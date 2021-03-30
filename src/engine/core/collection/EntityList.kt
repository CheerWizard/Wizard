package engine.core.collection

import engine.core.ecs.Entity

class EntityList : ArrayList<Entity>() {

    override fun add(element: Entity): Boolean {
        return super.add(element.apply {
            id = size
        })
    }

    override fun add(index: Int, element: Entity) {
        super.add(index, element.apply {
            id = size
        })
    }

    override fun addAll(elements: Collection<Entity>): Boolean {
        var newId = size
        for (element in elements) {
            element.id = newId++
        }

        return super.addAll(elements)
    }

    override fun addAll(index: Int, elements: Collection<Entity>): Boolean {
        var newId = size
        for (element in elements) {
            element.id = newId++
        }

        return super.addAll(index, elements)
    }

    fun lastId(): Int = size - 1

}