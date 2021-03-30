package engine.graphics.shader.uniforms

import engine.core.Updatable
import engine.graphics.math.Color4f

class UColor4f(
    var name: String = "",
    red: Float = 0f,
    green: Float = 0f,
    blue: Float = 0f,
    alpha: Float = 0f
) : Updatable<Color4f>(
    data = Color4f(
        red = red,
        green = green,
        blue = blue,
        alpha = alpha
    )
) {
    constructor(color: Color4f) : this(red = color.x, green = color.y, blue = color.z, alpha = color.w)
}