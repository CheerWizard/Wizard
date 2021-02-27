package application.platform.mesh

import application.graphics.mesh.Vertex
import application.graphics.mesh.VertexBuffer
import org.lwjgl.opengl.GL30.*

class GLVertexBuffer(
    vertex: Vertex,
    data: FloatArray
) : VertexBuffer(
    data = data,
    vertex = vertex
) {

    override var id: Int = glGenBuffers()

    override fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id)
        glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW)
        glVertexAttribPointer(vertex.attribute, vertex.typeSize, GL_FLOAT, false, 0, 0)
    }

    override fun unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    override fun onDestroy() {
        glDeleteBuffers(id)
    }

}