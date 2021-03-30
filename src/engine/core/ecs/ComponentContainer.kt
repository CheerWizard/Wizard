package engine.core.ecs

import engine.core.collection.DestroyableHashMap

interface ComponentContainer {

    val components : DestroyableHashMap<Short, Component>

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

}