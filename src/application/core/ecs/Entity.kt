package application.core.ecs

import application.core.Destroyable
import application.core.math.TransformMatrix4f
import application.graphics.component.TextureComponent

class Entity(
    val transformation: TransformMatrix4f,
    var textureComponent: TextureComponent? = null,
    var textureGridId: Int = 0
) : Destroyable {
    override fun onDestroy() {
        textureComponent?.onDestroy()
    }
}