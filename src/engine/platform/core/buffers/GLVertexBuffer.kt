package engine.platform.core.buffers

import engine.core.tools.Environment
import engine.graphics.core.buffers.VertexBuffer
import org.lwjgl.opengl.GL33.*

class GLVertexBuffer(
    totalVertexCount: Int = Environment.VERTEX_COUNT
) : VertexBuffer(
    totalVertexCount = totalVertexCount
) {

    companion object {
        const val FILL_MODE = GL_FILL
        const val LINE_MODE = GL_LINE
        const val POINT_MODE = GL_POINT
    }

    override var polygonMode: Int = FILL_MODE

    override var id: Int = glGenBuffers()

    override fun onPrepare() {
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STREAM_DRAW)
        createAttributes()
    }

    private fun createAttributes() {
        val stride = attributes.getByteSize()

        var totalAttributeByteSize = 0
        for (i in attributes.indices) {
            val attribute = attributes[i]

            for (j in 0 until attribute.count) {
                glVertexAttribPointer(
                    attribute.location,
                    attribute.size(),
                    GL_FLOAT,
                    false,
                    stride,
                    totalAttributeByteSize.toLong()
                )

                val attributeLocation = if (attribute.count > 1) attribute.location + j else attribute.location
                glVertexAttribDivisor(attributeLocation, attribute.type)

                totalAttributeByteSize += attribute.size() * Float.SIZE_BYTES
            }
        }
    }

    override fun enableAttributes() {
        for (attribute in attributes) {
            for (attributeId in 0 until attribute.count) {
                glEnableVertexAttribArray(attribute.location + attributeId)
            }
        }
    }

    override fun disableAttributes() {
        for (attribute in attributes) {
            for (attributeId in 0 until attribute.count) {
                glDisableVertexAttribArray(attribute.location + attributeId)
            }
        }
    }

    override fun onBind() {
        glBindBuffer(GL_ARRAY_BUFFER, id)
    }

    override fun onUpdate() {
        glBufferSubData(GL_ARRAY_BUFFER, subBufferOffset, subBuffer)
    }

    override fun onUnbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        glDeleteBuffers(id)
    }

}