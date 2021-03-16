package application.platform.core.buffers

import application.core.tools.Environment
import application.graphics.core.buffers.IndexBuffer
import org.lwjgl.opengl.GL15.*

class GLIndexBuffer(
    totalIndexCount: Int = Environment.INDEX_COUNT,
) : IndexBuffer(
        totalIndexCount = totalIndexCount,
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