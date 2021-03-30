package engine.graphics.shader.attributes

import org.joml.Vector3f

class Attribute3f(
    name: String = "",
    location: Int = 0,
    count: Int = 1,
    type: Int = VERTEX_TYPE,
    data: Vector3f = Vector3f()
) : Attribute(
    name = name,
    location = location,
    count = count,
    type = type,
    data = floatArrayOf(data.x, data.y, data.z)
)