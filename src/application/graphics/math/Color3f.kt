package application.graphics.math

import application.core.math.Vector3f

class Color3f(
    red: Float = 0f,
    green: Float = 0f,
    blue: Float = 0f
) : Vector3f() {
    init {
        x = red
        y = green
        z = blue
    }

    companion object {
        fun white(): Color3f = Color3f(
            red = 1f,
            green = 1f,
            blue = 1f
        )
        fun red(): Color3f = Color3f(
            red = 1f,
            green = 0f,
            blue = 0f
        )
    }
}