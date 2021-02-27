package application.core.ecs

import application.core.Destroyable

interface Component : Destroyable {
    fun getId(): Short
}