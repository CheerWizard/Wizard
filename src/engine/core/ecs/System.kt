package engine.core.ecs

import engine.core.Destroyable
import engine.core.collection.DestroyableHashMap
import engine.core.collection.DestroyableList

abstract class System(
    protected val entityGroups: DestroyableList<EntityGroup> = DestroyableList(),
    protected val sceneComponents: DestroyableHashMap<Short, SceneComponent> = DestroyableHashMap()
) : Destroyable, Comparable<System> {

    companion object {
        const val INCOMPATIBLE_ENTITY_GROUP: Int = -1
    }

    abstract val tag: String
    abstract val priority: Byte

    override fun compareTo(other: System): Int {
        return when {
            priority > other.priority -> 1
            priority < other.priority -> -1
            else -> 0
        }
    }

    open fun onPrepare() {
        for (entityGroupId in entityGroups.indices) {
            onPrepareEntityGroup(entityGroupId)
        }
    }

    protected open fun onPrepareEntityGroup(entityGroupId: Int) {
        for (entityId in entityGroups[entityGroupId].entities.indices) {
            onPrepareEntity(entityGroupId = entityGroupId, entityId = entityId)
        }
    }

    protected abstract fun onPrepareEntity(entityGroupId: Int, entityId: Int)

    open fun onUpdate() {
        updateSceneComponents()
        for (entityGroupId in entityGroups.indices) {
            onUpdateEntityGroup(entityGroupId)
        }
    }

    protected abstract fun updateSceneComponents()

    protected open fun onUpdateEntityGroup(entityGroupId: Int) {
        for (entityId in entityGroups[entityGroupId].entities.indices) {
            onUpdateEntity(entityGroupId = entityGroupId, entityId = entityId)
        }
    }

    protected abstract fun onUpdateEntity(entityGroupId: Int, entityId: Int)

    fun addEntityGroup(entityGroup: EntityGroup) : Int {
        if (!entityGroup.contains(getRequiredComponentId())) return INCOMPATIBLE_ENTITY_GROUP

        entityGroups.add(entityGroup)
        return entityGroups.size - 1
    }

    protected abstract fun getRequiredComponentId() : Short

    fun getEntityGroup(entityGroupId: Int) : EntityGroup = entityGroups[entityGroupId]

    fun getEntity(entityGroupId: Int, entityId: Int) : Entity = entityGroups[entityGroupId].getEntity(entityId)

    fun addComponent(entityGroupId: Int, component: Component) {
        entityGroups[entityGroupId].apply {
            putComponent(component)
        }
    }

    fun setEntityGroup(entityGroup: EntityGroup, entityGroupId: Int) {
        entityGroups[entityGroupId] = entityGroup
    }

    fun setComponent(entityGroupId: Int, component: Component) {
        addComponent(entityGroupId, component)
    }

    fun removeEntityGroup(entityGroupId: Int) {
        entityGroups.removeAt(entityGroupId)
    }

    fun removeComponent(entityGroupId: Int, componentId: Short) {
        entityGroups[entityGroupId].run {
            removeComponent(componentId)
        }
    }

    fun removeComponents(entityGroupId: Int) {
        entityGroups[entityGroupId].run {
            removeComponents()
        }
    }

    fun addSceneComponent(sceneComponent: SceneComponent) {
        sceneComponents[sceneComponent.getId()] = sceneComponent
    }

    fun removeSceneComponent(sceneComponentId: Short) {
        sceneComponents.remove(sceneComponentId)
    }

    fun<T : Component> getComponent(entityGroupId: Int, componentId: Short): T? = entityGroups[entityGroupId].getComponent(componentId)

    fun<T : Component> getNonNullComponent(entityGroupId: Int, componentId: Short) : T = entityGroups[entityGroupId].getNonNullComponent(componentId)

    fun<T : SceneComponent> getSceneComponent(sceneComponentId: Short) : T? = sceneComponents[sceneComponentId] as T?

    fun<T : SceneComponent> getNonNullSceneComponent(sceneComponentId: Short) : T = sceneComponents[sceneComponentId] as T

    override fun onDestroy() {
        entityGroups.clear()
        sceneComponents.clear()
    }

}