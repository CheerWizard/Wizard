package application.core.ecs

import application.core.collection.DestroyableMap
import application.core.math.TransformComponent

class Entity : ComponentContainer {

    var id: Int = 0
    override val components = DestroyableMap<Short, Component>()

    init {
        putComponent(TransformComponent())
    }

}