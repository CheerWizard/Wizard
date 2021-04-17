package engine.core.ecs

import engine.core.Destroyable
import engine.core.collection.DestroyableHashMap
import engine.core.collection.DestroyableTreeMap
import engine.core.tools.FpsTicker
import engine.core.tools.VideoCard
import engine.graphics.math.Color4f
import engine.graphics.math.Colors
import engine.graphics.tools.ObjParser
import engine.graphics.tools.ShaderFactory
import engine.graphics.tools.TerrainParser
import engine.window.Cursor
import engine.window.Window
import imgui.ImGuiWindow

abstract class Engine(protected var client: Client) : Destroyable {

    interface Client : Cursor.Listener, Window.Listener {
        fun onCreate()
        fun onBindIOController()
        fun onUpdate()
        fun onDestroy()
        fun isOpen(): Boolean
        fun createMaxFps(): Long
    }

    var backgroundColor: Color4f = Colors.fromHex("#041F60")

    abstract val shaderFactory: ShaderFactory
    abstract var videoCard: VideoCard

    private val windows = DestroyableHashMap<String, Window>()

    private val systems: DestroyableTreeMap<String, System> = DestroyableTreeMap()

    val objParser: ObjParser = ObjParser()
    val terrainParser: TerrainParser = TerrainParser()

    fun getWindow(title: String): Window? = windows[title]

    fun getNonNullWindow(title: String): Window = windows[title] as Window

    fun createWindow(title: String): Window {
        val window = Window(title = title).apply {
            onCreate()
            show()
        }

        windows[title] = window

        return window
    }

    fun createImGuiWindow(title: String): ImGuiWindow {
        val window = ImGuiWindow(title = title).apply {
            onCreate()
            show()
        }

        windows[title] = window

        return window
    }

    fun removeWindow(title: String) {
        windows.remove(title)
    }

    fun removeWindows() {
        windows.clear()
    }

    fun setMaxFps(fps: Long) {
        FpsTicker.maxFps = fps
    }

    fun addEntityGroup(systemTag: String, entityGroup: EntityGroup): Int {
        val system = systems[systemTag]
        if (system != null) {
            return system.addEntityGroup(entityGroup)
        }
        return -1
    }

    fun run() {
        onCreate()
        while (client.isOpen()) {
            onUpdate()
        }
        onDestroy()
    }

    protected open fun onCreate() {
        createLibraries()

        client.run {
            onCreate()
            onBindIOController()
        }

        for (system in systems.values) {
            system.run {
                onPrepare()
            }
        }

        FpsTicker.maxFps = client.createMaxFps()

        enableDepthTest()
        enableTransparency()
    }

    protected abstract fun createLibraries()

    protected open fun onUpdate() {
        FpsTicker.deltaTimeOf {
            for (window in windows.values) {
                window.onUpdate()
            }

            client.onUpdate()

            for (system in systems.values) {
                system.onUpdate()
            }
        }
    }

    fun<T : System> putSystem(system: T) {
        systems[system.tag] = system
    }

    fun<T : System> getSystem(systemTag: String): T = systems[systemTag] as T

    fun<T : System> getEntityGroup(
        systemTag: String,
        entityGroupId: Int
    ): EntityGroup = getSystem<T>(systemTag).getEntityGroup(entityGroupId = entityGroupId)

    fun<T : System> getEntity(
        systemTag: String,
        entityGroupId: Int,
        entityId: Int
    ) : Entity = getEntityGroup<T>(systemTag = systemTag, entityGroupId = entityGroupId).getEntity(entityId)

    fun<T : System, R : Component> getComponent(
        systemTag: String,
        entityGroupId: Int,
        componentId: Short
    ) : R? = getEntityGroup<T>(systemTag = systemTag, entityGroupId = entityGroupId).getComponent(componentId)

    fun<T : System, R : Component> getNonNullComponent(
        systemTag: String,
        entityGroupId: Int,
        componentId: Short
    ) : R = getEntityGroup<T>(systemTag = systemTag, entityGroupId = entityGroupId).getNonNullComponent(componentId)

    fun<T : System, R : SceneComponent> getSceneComponent(
        systemTag: String,
        sceneComponentId: Short
    ) : R? = getSystem<T>(systemTag).getSceneComponent(sceneComponentId)

    fun<T : System, R : SceneComponent> getNonNullSceneComponent(
        systemTag: String,
        sceneComponentId: Short
    ) : R = getSystem<T>(systemTag).getNonNullSceneComponent(sceneComponentId)

    abstract fun createRender3d(windowTitle: String)

    override fun onDestroy() {
        client.onDestroy()
        systems.clear()
        removeWindows()
    }

    abstract fun enableDepthTest()
    abstract fun enableTransparency()
    abstract fun createVideoCard()

}