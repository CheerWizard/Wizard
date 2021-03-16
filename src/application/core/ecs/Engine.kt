package application.core.ecs

import application.core.Destroyable
import application.core.collection.DestroyableMap
import application.graphics.tools.ShaderFactory
import application.core.tools.VideoCard

abstract class Engine(protected var listener: Listener) : Thread(), Destroyable {

    interface Listener {
        fun isOpen(): Boolean
        fun createWindow()
        fun onCreate()
        fun onUpdate()
        fun onDestroy()
        fun getWidth(): Float
        fun getHeight(): Float
    }

    abstract val shaderFactory: ShaderFactory
    abstract val videoCard: VideoCard

    protected val systems: DestroyableMap<Byte, System> = DestroyableMap()

    var fps: Long = 60

    fun addEntityGroup(systemId: Byte, entityGroup: EntityGroup): Int {
        val system = systems[systemId]
        if (system != null) {
            return system.addEntityGroup(entityGroup)
        }
        return -1
    }

    override fun run() {
        onCreate()
        while (listener.isOpen()) {
            onUpdate()
        }
        onDestroy()
    }

    protected open fun onCreate() {
        listener.createWindow()
        createNativeCapabilities()
        listener.onCreate()
        for (system in systems.values) {
            system.run {
                onPrepare()
            }
        }
    }

    protected abstract fun createNativeCapabilities()

    protected open fun onUpdate() {
        val startFrameTime = java.lang.System.currentTimeMillis()

        for (system in systems.values) {
            system.onUpdate()
        }
        listener.onUpdate()

        val deltaFrameTime = java.lang.System.currentTimeMillis() - startFrameTime
        val maxFrameTime = 1000 / fps

        if (deltaFrameTime <= maxFrameTime) {
            sleep(maxFrameTime)
        }
    }

    fun putSystem(system: System) {
        systems[system.getId()] = system
    }

    fun<T : System> getSystem(systemId: Byte): T = systems[systemId] as T

    fun<T : System> getEntityGroup(
        systemId: Byte,
        entityGroupId: Int
    ): EntityGroup = (systems[systemId] as T).getEntityGroup(entityGroupId = entityGroupId)

    fun<T : System> getEntity(
        systemId: Byte, entityGroupId: Int,
        entityId: Int
    ) : Entity = getEntityGroup<T>(systemId = systemId, entityGroupId = entityGroupId).getEntity(entityId)

    fun<T : System, R : Component> getComponent(
        systemId: Byte,
        entityGroupId: Int,
        componentId: Short
    ) : R? = getEntityGroup<T>(systemId = systemId, entityGroupId = entityGroupId).getComponent<R>(componentId)

    fun<T : System, R : Component> getNonNullComponent(
        systemId: Byte,
        entityGroupId: Int,
        componentId: Short
    ) : R = getEntityGroup<T>(systemId = systemId, entityGroupId = entityGroupId).getNonNullComponent(componentId)

    fun<T : System, R : SceneComponent> getSceneComponent(
        systemId: Byte,
        sceneComponentId: Short
    ) : R? = (systems[systemId] as T).getSceneComponent(sceneComponentId)

    fun<T : System, R : SceneComponent> getNonNullSceneComponent(
        systemId: Byte,
        sceneComponentId: Short
    ) : R = (systems[systemId] as T).getNonNullSceneComponent(sceneComponentId)

    override fun onDestroy() {
        systems.clear()
        listener.onDestroy()
    }

}