package engine.core.math

import engine.graphics.math.Rotator3f
import engine.graphics.shader.uniforms.UMatrix4f
import org.joml.Vector3f

class TransformMatrix4f(
    name: String = "",
    val position: Vector3f = Vector3f(),
    val rotation: Rotator3f = Rotator3f(),
    var scalar: Vector3f = Vector3f(1f, 1f, 1f)
) : UMatrix4f(name = name) {

    init {
        applyChanges()
    }

    override fun applyChanges() {
        super.applyChanges()
        identity()
        translate(position)
        rotate(rotation.x, 1f, 0f, 0f)
        rotate(rotation.y, 0f, 1f, 0f)
        rotate(rotation.z, 0f, 0f, 1f)
        scale(scalar)
    }

}