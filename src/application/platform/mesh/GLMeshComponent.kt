package application.platform.mesh

import application.core.collection.DestroyableList
import application.graphics.mesh.MeshComponent
import application.graphics.mesh.VertexBuffer
import org.lwjgl.opengl.GL30.*

class GLMeshComponent(
    vertexBuffers: DestroyableList<VertexBuffer> = DestroyableList(),
    indices: IntArray = intArrayOf()
) : MeshComponent(
    vertexBuffers = vertexBuffers,
    indices = indices
) {

    companion object {
        const val FILL_MODE = GL_FILL
        const val LINE_MODE = GL_LINE
        const val POINT_MODE = GL_POINT
    }

    override var polygonMode: Int = FILL_MODE

    override var vertexArrayId: Int = glGenVertexArrays()
    override var indexBufferId: Int = glGenBuffers()

    override fun bindVertexArray() {
        glBindVertexArray(vertexArrayId)
    }

    override fun unbindVertexArray() {
        glBindVertexArray(0)
    }

    override fun bindIndexBuffer() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW)
    }

    override fun unbindIndexBuffer() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        glDeleteBuffers(indexBufferId)
        glDeleteVertexArrays(vertexArrayId)
    }

}