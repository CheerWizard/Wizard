package engine.graphics.shader.attributes

import org.joml.Matrix4f

class Attribute16f(
    name: String = "",
    location: Int = 0,
    count: Int = 1,
    type: Int = VERTEX_TYPE,
    data: Matrix4f = Matrix4f()
) : Attribute(
    name = name,
    location = location,
    count = count,
    type = type,
    data = floatArrayOf(
        data.m00(), data.m01(), data.m02(), data.m03(),
        data.m10(), data.m11(), data.m12(), data.m13(),
        data.m20(), data.m21(), data.m22(), data.m23(),
        data.m30(), data.m31(), data.m32(), data.m33(),
    )
)