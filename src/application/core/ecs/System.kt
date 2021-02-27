package application.core.ecs

import application.core.Destroyable
import application.core.collection.DestroyableList

abstract class System(protected val entityGroups: DestroyableList<EntityGroup> = DestroyableList()) : Destroyable {

    abstract fun getId(): Byte
    abstract fun onCreate()

    open fun onPrepare() {
        for (entityGroup in entityGroups) {
            onPrepare(entityGroup)
        }
    }

    protected open fun onPrepare(entityGroup: EntityGroup) {
        for (entity in entityGroup.entities) {
            onPrepare(entity)
        }
    }

    protected abstract fun onPrepare(entity: Entity)

    open fun onUpdate() {
        for (entityGroup in entityGroups) {
            onUpdate(entityGroup)
        }
    }

    protected open fun onUpdate(entityGroup: EntityGroup) {
        for (entity in entityGroup.entities) {
            onUpdate(entity)
        }
    }

    protected abstract fun onUpdate(entity: Entity)

    fun addEntityGroup(entityGroup: EntityGroup) : Int {
        entityGroups.add(entityGroup)
        return entityGroups.size - 1
    }

    fun isCompatible(entityGroup: EntityGroup) : Boolean = entityGroup.contains(getRequiredComponentId())

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

    fun<T : Component> getComponent(entityGroupId: Int, componentId: Short): T? = entityGroups[entityGroupId].getComponent(componentId)

    fun<T : Component> getNonNullComponent(entityGroupId: Int, componentId: Short) : T = entityGroups[entityGroupId].getNonNullComponent(componentId)

    override fun onDestroy() {
        entityGroups.clear()
    }

}