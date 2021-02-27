package application.core.ecs

import application.core.Destroyable
import application.core.collection.DestroyableList
import application.core.collection.DestroyableMap

open class EntityGroup : Destroyable {

    val entities = DestroyableList<Entity>()

    protected val components = DestroyableMap<Short, Component>()

    fun getEntity(entityId: Int): Entity = entities[entityId]

    fun contains(componentId: Short): Boolean = components.containsKey(componentId)

    fun putComponent(component: Component) {
        components[component.getId()] = component
    }

    fun<T : Component> getComponent(componentId: Short): T? = components[componentId] as T?

    fun<T : Component> getNonNullComponent(componentId: Short): T = components[componentId] as T

    fun removeComponent(componentId: Short) {
        components.remove(componentId)
    }

    fun removeComponents() {
        components.clear()
    }

    fun addEntity(entity: Entity): Int {
        entities.add(entity)
        return entities.size - 1
    }

    fun removeEntity(entityId: Int) {
        entities.removeAt(entityId)
    }

    fun removeEntities() {
        entities.clear()
    }

    fun addEntities(entities: List<Entity>) {
        this.entities.addAll(entities)
    }

    override fun onDestroy() {
        removeEntities()
        removeComponents()
    }

}