package application.graphics.shader.uniforms

import application.graphics.math.Color4f

class UColor4f(
    var name: String = "",
    red: Float = 0f,
    green: Float = 0f,
    blue: Float = 0f,
    alpha: Float = 0f
) : Color4f(
    red = red,
    green = green,
    blue = blue,
    alpha = alpha
)