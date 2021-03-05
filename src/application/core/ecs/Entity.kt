package application.core.ecs

import application.core.Destroyable
import application.core.math.TransformMatrix4f
import application.graphics.material.MaterialComponent

class Entity(
    val transformation: TransformMatrix4f,
    var materialComponent: MaterialComponent? = null
) : Destroyable {
    override fun onDestroy() {
        materialComponent?.onDestroy()
    }
}