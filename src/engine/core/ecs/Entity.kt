package engine.core.ecs

import engine.core.collection.DestroyableHashMap

class Entity : ComponentContainer {
    var id: Int = 0
    override val components = DestroyableHashMap<Short, Component>()
}