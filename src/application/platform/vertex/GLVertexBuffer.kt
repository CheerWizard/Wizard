package application.platform.vertex

import application.graphics.geometry.VertexBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL30.*

class GLVertexBuffer(
    totalMeshCount: Int = DEFAULT_TOTAL_MESH_COUNT,
    averageMeshSize: Int = DEFAULT_AVERAGE_MESH_SIZE
) : VertexBuffer(
    totalMeshCount = totalMeshCount,
    averageMeshSize = averageMeshSize
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
        createAttributePointers()
    }

    private fun createAttributePointers() {
        var totalByteSize = 0
        for (attribute in attributes) {
            totalByteSize += attribute.byteSize()
        }

        var offset = 0
        for (i in attributes.indices) {
            val attribute = attributes[i]
            val attributePointer = BufferUtils.createFloatBuffer(offset)
            glVertexAttribPointer(attribute.location, attribute.size(), GL_FLOAT, false, totalByteSize, attributePointer)
            offset += attribute.byteSize()
        }
    }

    override fun enableAttributes() {
        for (attribute in attributes) {
            glEnableVertexAttribArray(attribute.location)
        }
    }

    override fun disableAttributes() {
        for (attribute in attributes) {
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