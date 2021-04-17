package engine.graphics.math

import org.joml.Vector4f

open class Color4f(
    red: Float = 0f,
    green: Float = 0f,
    blue: Float = 0f,
    alpha: Float = 1f
) : Vector4f(
    red,
    green,
    blue,
    alpha
) {

    fun toArray(): FloatArray = floatArrayOf(x, y, z, w)

    companion object {
        fun white(): Color4f = Color4f(
            red = 1f,
            green = 1f,
            blue = 1f,
            alpha = 1f
        )
        fun black(): Color4f = Color4f(
            red = 0f,
            green = 0f,
            blue = 0f,
            alpha = 1f
        )
    }

}