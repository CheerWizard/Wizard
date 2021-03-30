package engine.graphics.shader.uniforms

class UColor3f(
    name: String = "",
    red: Float = 0f,
    green: Float = 0f,
    blue: Float = 0f
) : UVector3f(
    name = name,
    x = red,
    y = green,
    z = blue
)