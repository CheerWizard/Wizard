package engine.graphics.math

import engine.graphics.shader.uniforms.UMatrix4f

open class ProjectionMatrix4f(
    name: String = "",
    var screenWidth: Float,
    var screenHeight: Float
) : UMatrix4f(name = name)