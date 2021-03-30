package engine.graphics.shader.uniforms

import engine.core.Updatable
import org.joml.Vector4f

class UVector4f(
    var name: String = "",
    x: Float = 0f,
    y: Float = 0f,
    z: Float = 0f,
    w: Float = 1f
) : Updatable<Vector4f>(
    data = Vector4f(
        x,
        y,
        z,
        w
    )
)