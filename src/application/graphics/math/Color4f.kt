package application.graphics.math

import application.core.math.Vector4f

class Color4f(
    red: Float = 1f,
    green: Float = 1f,
    blue: Float = 1f,
    alpha: Float = 1f
) : Vector4f() {
    init {
        x = red
        y = green
        z = blue
        w = alpha
    }
}