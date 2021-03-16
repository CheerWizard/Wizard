package application.graphics.shader.uniforms

import org.joml.Vector3f

class UVector3f(
    var name: String = "",
    x: Float = 0f,
    y: Float = 0f,
    z: Float = 0f
) : Vector3f(x, y, z)