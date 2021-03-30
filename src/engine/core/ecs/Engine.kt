package engine.core.ecs

import engine.core.Destroyable
import engine.core.collection.DestroyableTreeMap
import engine.core.tools.FpsTicker
import engine.core.tools.VideoCard
import engine.graphics.math.Color4f
import engine.graphics.math.Colors
import engine.graphics.tools.ObjParser
import engine.graphics.tools.ShaderFactory
import engine.graphics.tools.TerrainParser
import engine.window.Cursor
import engine.window.IOController
import engine.window.Window

abstract class Engine(title: String, protected var client: Client) : Thread(), Destroyable {

    interface Client : Cursor.Listener, Window.Listener {
        fun onCreate()
        fun onUpdate()
        fun onDestroy()
    }

    val window: Window = Window(title = title)

    var backgroundColor: Color4f = Colors.fromHex("#041F60")

    abstract val shaderFactory: ShaderFactory
    abstract var videoCard: VideoCard

    private val systems: DestroyableTreeMap<String, System> = DestroyableTreeMap()

    val ioController = IOController()

    val objParser: ObjParser = ObjParser()
    val terrainParser: TerrainParser = TerrainParser()

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

    override fun run() {
        onCreate()
        while (window.isOpen()) {
            onUpdate()
        }
        onDestroy()
    }

    protected open fun onCreate() {
        createLibraries()

        window.run {
            setViewPort()
            setInputController(ioController)
            setWindowListener(client)
            setCursorListener(client)
            show()
        }

        FpsTicker.maxFps = window.getRefreshRate().toLong()

        client.onCreate()

        for (system in systems.values) {
            system.run {
                onPrepare()
            }
        }
    }

    protected abstract fun createLibraries()

    protected open fun onUpdate() {
        FpsTicker.deltaTimeOf {
            clearDisplay()

            window.pollEvents()

            ioController.onUpdate()

            client.onUpdate()

            for (system in systems.values) {
                system.onUpdate()
            }

            window.swapBuffers()
        }
    }

    protected abstract fun clearDisplay()

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

    override fun onDestroy() {
        ioController.onDestroy()
        client.onDestroy()
        systems.clear()
        window.onDestroy()
    }

}