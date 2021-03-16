package application.graphics.shader.uniforms

import org.joml.Vector4f

class UVector4f(
    var name: String = "",
    x: Float = 0f,
    y: Float = 0f,
    z: Float = 0f,
    w: Float = 1f
) : Vector4f(
    x,
    y,
    z,
    w
)