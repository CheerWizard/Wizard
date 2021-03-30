package engine.graphics.math

import java.awt.Color

object Colors {

    fun fromHex(hex: String) : Color4f {
        val color = Color.decode(hex)
        val r: Float = color.red.toFloat() / 255
        val g: Float = color.green.toFloat() / 255
        val b: Float = color.blue.toFloat() / 255
        return Color4f(red = r, green = g, blue = b , alpha = 1f)
    }

    fun fromHex(hex: String, alpha: Float): Color4f = fromHex(hex).apply {
        w = alpha
    }

    fun fromHex(hex: String, alpha: Int): Color4f = fromHex(hex = hex, alpha = alpha.toFloat() / 100)

}