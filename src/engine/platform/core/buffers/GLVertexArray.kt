package engine.platform.core.buffers

import engine.graphics.core.buffers.VertexArray
import org.lwjgl.opengl.GL30.*

class GLVertexArray : VertexArray() {

    override var id: Int = glGenVertexArrays()

    override fun bind() {
        glBindVertexArray(id)
    }

    override fun unbind() {
        glBindVertexArray(0)
    }

    override fun onDestroy() {
        glDeleteVertexArrays(id)
    }

}