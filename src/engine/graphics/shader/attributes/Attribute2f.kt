package engine.graphics.shader.attributes

import org.joml.Vector2f

class Attribute2f(
    name: String = "",
    location: Int = 0,
    count: Int = 1,
    type: Int = VERTEX_TYPE,
    data: Vector2f = Vector2f()
) : Attribute(
    name = name,
    location = location,
    count = count,
    type = type,
    data = floatArrayOf(data.x, data.y)
)