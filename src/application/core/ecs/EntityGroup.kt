package application.core.ecs

import application.core.Destroyable
import application.core.collection.DestroyableMap
import application.core.collection.EntityList

open class EntityGroup : ComponentContainer, Destroyable {

    override val components: DestroyableMap<Short, Component> = DestroyableMap()

    val entities = EntityList()

    fun getEntity(entityId: Int): Entity = entities[entityId]

    fun addEntity(entity: Entity): Int {
        entities.add(entity)
        return entities.lastId()
    }

    fun removeEntity(entityId: Int) = entities.removeAt(entityId)

    fun removeEntities() = entities.clear()

    fun addEntities(entities: List<Entity>) = this.entities.addAll(entities)

    override fun onDestroy() {
        removeEntities()
        removeComponents()
    }

}