package application.platform.obj

import application.graphics.mesh.VertexBuffer
import application.graphics.obj.ObjParser
import application.platform.mesh.GLVertex
import application.platform.mesh.GLVertexBuffer

class GLObjParser : ObjParser() {
    override fun createNormalBuffer(name: String): VertexBuffer = GLVertexBuffer(
        data = normalsArray,
        vertex = GLVertex(
            name = name,
            typeSize = 3
        )
    )
    override fun createVertexBuffer(name: String): VertexBuffer = GLVertexBuffer(
        data = vertices.toFloatArray(),
        vertex = GLVertex(
            name = name,
            typeSize = 3
        )
    )
    override fun createTextureBuffer(name: String): VertexBuffer = GLVertexBuffer(
        data = textureArray,
        vertex = GLVertex(
            name = name,
            typeSize = 2
        )
    )
    override fun createTangentsBuffer(name: String): VertexBuffer = GLVertexBuffer(
        data = tangentsArray,
        vertex = GLVertex(
            name = name,
            typeSize = 3
        )
    )
}