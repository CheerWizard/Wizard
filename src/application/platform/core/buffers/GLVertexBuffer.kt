package application.platform.core.buffers

import application.core.tools.Environment
import application.graphics.core.buffers.VertexBuffer
import application.graphics.shader.Attribute
import org.lwjgl.opengl.GL30.*

class GLVertexBuffer(
    totalVertexCount: Int = Environment.VERTEX_COUNT,
    attribute: Attribute
) : VertexBuffer(
        totalVertexCount = totalVertexCount,
        attribute = attribute
) {

    companion object {
        const val FILL_MODE = GL_FILL
        const val LINE_MODE = GL_LINE
        const val POINT_MODE = GL_POINT
    }

    override var polygonMode: Int = FILL_MODE

    override var id: Int = glGenBuffers()

    override fun prepare() {
        glBindBuffer(GL_ARRAY_BUFFER, id)
        glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW)
        createAttributePointer()
    }

    private fun createAttributePointer() {
        glVertexAttribPointer(
                attribute.location,
                attribute.size(),
                GL_FLOAT,
                false,
                0,
                0
        )
    }

    override fun enableAttributes() {
        glEnableVertexAttribArray(attribute.location)
    }

    override fun disableAttributes() {
        glDisableVertexAttribArray(attribute.location)
    }

    override fun bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id)
        glBufferSubData(GL_ARRAY_BUFFER, 0, data)
    }

    override fun unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        glDeleteBuffers(id)
    }

}