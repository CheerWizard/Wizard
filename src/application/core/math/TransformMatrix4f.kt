package application.core.math

import application.graphics.math.Rotator3f
import org.joml.Matrix4f
import org.joml.Vector3f

class TransformMatrix4f(
    val name: String = "",
    val position: Vector3f = Vector3f(),
    val rotation: Rotator3f = Rotator3f(),
    var scalar: Vector3f = Vector3f(1f, 1f, 1f)
) : Matrix4f() {

    var isUpdated = false

    init {
        apply()
    }

    fun apply() {
        isUpdated = true
        identity()
        translate(position)
        rotate(rotation.x, 1f, 0f, 0f)
        rotate(rotation.y, 0f, 1f, 0f)
        rotate(rotation.z, 0f, 0f, 1f)
        scale(scalar)
    }

}