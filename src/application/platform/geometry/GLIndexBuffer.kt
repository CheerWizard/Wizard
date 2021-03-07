package application.platform.geometry

import application.graphics.geometry.IndexBuffer
import org.lwjgl.opengl.GL15.*

class GLIndexBuffer(
        totalVertexCount: Int = MAX_VERTEX_COUNT,
) : IndexBuffer(
        totalVertexCount = totalVertexCount,
) {

    override var id: Int = glGenBuffers()

    override fun prepare() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW)
    }

    override fun bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, data)
    }

    override fun unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        glDeleteBuffers(id)
    }

}