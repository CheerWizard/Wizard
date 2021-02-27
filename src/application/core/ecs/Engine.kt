package application.core.ecs

import application.core.Destroyable
import application.core.collection.DestroyableMap

abstract class Engine(protected var editor: Editor) : Thread(), Destroyable {

    interface Editor {
        fun isOpen(): Boolean
        fun createWindow()
        fun onCreate()
        fun onUpdate()
        fun onDestroy()
        fun getWidth(): Float
        fun getHeight(): Float
    }

    protected val systems: DestroyableMap<Byte, System> = DestroyableMap()

    fun addEntityGroup(systemId: Byte, entityGroup: EntityGroup): Int {
        val system = systems[systemId]
        if (system != null) {
            return system.addEntityGroup(entityGroup)
        }
        return -1
    }

    override fun run() {
        onCreate()
        onPrepare()
        while (editor.isOpen()) {
            onUpdate()
        }
        onDestroy()
    }

    protected open fun onCreate() {
        editor.createWindow()
        createNativeCapabilities()
        editor.onCreate()
        for (system in systems.values) {
            system.onCreate()
        }
    }

    protected open fun onPrepare() {
        for (system in systems.values) {
            system.onPrepare()
        }
    }

    protected abstract fun createNativeCapabilities()

    protected open fun onUpdate() {
        for (system in systems.values) {
            system.onUpdate()
        }
        editor.onUpdate()
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

    override fun onDestroy() {
        systems.clear()
        editor.onDestroy()
    }

}