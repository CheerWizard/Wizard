package engine.platform.core.buffers

import engine.core.tools.Environment
import engine.graphics.core.buffers.IndexBuffer
import org.lwjgl.opengl.GL15.*

class GLIndexBuffer(
    totalIndexCount: Int = Environment.INDEX_COUNT,
) : IndexBuffer(
        totalIndexCount = totalIndexCount,
) {

    override var id: Int = glGenBuffers()

    override fun onPrepare() {
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STREAM_DRAW)
    }

    override fun onBind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)
    }

    override fun onUpdate() {
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, subBufferOffset, subBuffer)
    }

    override fun onUnbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        glDeleteBuffers(id)
    }

}