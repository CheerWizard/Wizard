package application.platform.obj

import application.graphics.obj.ObjParser
import application.graphics.geometry.VertexBuffer
import application.platform.vertex.GLVertexBuffer

class GLObjParser : ObjParser() {
    override fun createNormalBuffer(name: String): VertexBuffer = GLVertexBuffer()
    override fun createVertexBuffer(name: String): VertexBuffer = GLVertexBuffer()
    override fun createTextureBuffer(name: String): VertexBuffer = GLVertexBuffer()
    override fun createTangentsBuffer(name: String): VertexBuffer = GLVertexBuffer()
}