package application.core.math

import application.graphics.math.Rotator3f
import application.graphics.math.Translator3f

class TransformMatrix4f(
    val name: String,
    val position: Translator3f = Translator3f(),
    val rotation: Rotator3f = Rotator3f(),
    var scale: Float = 1f
) : Matrix4f() {

    init {
        apply()
    }

    fun apply() {
        identity()
        translate(position.x, position.y, position.z)
        rotate(rotation.x, 1f, 0f, 0f)
        rotate(rotation.y, 0f, 1f, 0f)
        rotate(rotation.z, 0f, 0f, 1f)
        scale(scale)
    }

}