package engine.graphics.shader.attributes

class Attribute1f(
    name: String = "",
    location: Int = 0,
    count: Int = 1,
    type: Int = VERTEX_TYPE,
    data: Float
) : Attribute(
    name = name,
    location = location,
    count = count,
    type = type,
    data = floatArrayOf(data)
)