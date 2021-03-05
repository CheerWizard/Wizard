package application.graphics.component

import application.core.ecs.Component
import application.graphics.vertex.IndexBuffer
import application.graphics.vertex.VertexArray
import application.graphics.vertex.VertexBuffer

open class MeshComponent(
    var vertexArray: VertexArray,
    var vertexBuffer: VertexBuffer,
    var indexBuffer: IndexBuffer
) : Component {

    var usesCullFace = true

    companion object {
        const val ID: Short = 2
    }

    fun getPolygonMode(): Int = vertexBuffer.polygonMode

    fun hasIndices(): Boolean = indexBuffer.capacity() > 0

    fun getIndexCount(): Int = indexBuffer.capacity()

    fun getVertexCount(): Int = vertexBuffer.capacity()

    override fun getId(): Short = ID

    fun create() {
        bindVertexArray()
        createIndexBuffer()
        createVertexBuffer()
        unbindVertexArray()
    }

    fun enableAttributes() {
        vertexBuffer.enableAttributes()
    }

    fun disableAttributes() {
        vertexBuffer.disableAttributes()
    }

    fun bindVertexArray() {
        vertexArray.bind()
    }

    fun unbindVertexArray() {
        vertexArray.unbind()
    }

    fun createIndexBuffer() {
        indexBuffer.create()
    }

    fun bindIndexBuffer() {
        indexBuffer.bind()
    }

    fun unbindIndexBuffer() {
        indexBuffer.unbind()
    }

    fun createVertexBuffer() {
        vertexBuffer.create()
    }

    fun bindVertexBuffer() {
        vertexBuffer.bind()
    }

    fun unbindVertexBuffer() {
        vertexBuffer.unbind()
    }

    override fun onDestroy() {
        vertexArray.onDestroy()
        vertexBuffer.onDestroy()
        indexBuffer.onDestroy()
    }

}