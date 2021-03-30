package engine.graphics.shader.attributes

import org.joml.Vector4f

class Attribute4f(
    name: String = "",
    location: Int = 0,
    count: Int = 1,
    type: Int = VERTEX_TYPE,
    data: Vector4f = Vector4f()
) : Attribute(
    name = name,
    location = location,
    count = count,
    type = type,
    data = floatArrayOf(data.x, data.y, data.z, data.w)
)