package engine.graphics.shader.uniforms

import engine.core.Updatable
import org.joml.Vector2f

class UVector2f(
    var name: String = "",
    x: Float = 0f,
    y: Float = 0f
) : Updatable<Vector2f>(
    data = Vector2f(x, y)
)