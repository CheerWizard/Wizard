package application.platform.vertex

import application.graphics.vertex.VertexBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL30.*

class GLVertexBuffer(capacity: Int = DEFAULT_CAPACITY) : VertexBuffer(capacity = capacity) {

    companion object {
        const val FILL_MODE = GL_FILL
        const val LINE_MODE = GL_LINE
        const val POINT_MODE = GL_POINT
    }

    override var polygonMode: Int = FILL_MODE

    override var id: Int = glGenBuffers()

    override fun create() {
        glBindBuffer(GL_ARRAY_BUFFER, id)
        glBufferData(GL_ARRAY_BUFFER, data, GL_DYNAMIC_DRAW)
        createAttributePointers()
    }

    private fun createAttributePointers() {
        var totalByteSize = 0
        for (attribute in attributes.values) {
            totalByteSize += attribute.byteSize()
        }

        for (attribute in attributes.values) {
            val attributePointer = BufferUtils.createFloatBuffer(attribute.size())
            glVertexAttribPointer(attribute.location, attribute.size(), GL_FLOAT, false, totalByteSize, attributePointer)
        }
    }

    override fun enableAttributes() {
        for (attribute in attributes.values) {
            glEnableVertexAttribArray(attribute.location)
        }
    }

    override fun disableAttributes() {
        for (attribute in attributes.values) {
            glDisableVertexAttribArray(attribute.location)
        }
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