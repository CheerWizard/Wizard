package engine.graphics.shader.uniforms

import engine.core.Updatable
import org.joml.Vector3f

open class UVector3f(
    var name: String = "",
    x: Float = 0f,
    y: Float = 0f,
    z: Float = 0f
) : Updatable<Vector3f>(
    data = Vector3f(x, y, z)
)