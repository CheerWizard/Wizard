package engine.core.ecs

import engine.core.Destroyable

interface Component : Destroyable {
    fun getId(): Short
}