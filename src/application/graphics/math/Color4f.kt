package application.graphics.math

import application.core.math.Vector4f

open class Color4f(
    red: Float = 0f,
    green: Float = 0f,
    blue: Float = 0f,
    alpha: Float = 0f
) : Vector4f() {

    init {
        x = red
        y = green
        z = blue
        w = alpha
    }

    companion object {
        fun white(): Color4f = Color4f(
            red = 1f,
            green = 1f,
            blue = 1f,
            alpha = 1f
        )
    }

}