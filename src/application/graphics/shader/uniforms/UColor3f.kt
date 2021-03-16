package application.graphics.shader.uniforms

import application.graphics.math.Color3f

class UColor3f(
    var name: String = "",
    red: Float = 0f,
    green: Float = 0f,
    blue: Float = 0f
) : Color3f(
    red = red,
    green = green,
    blue = blue
)