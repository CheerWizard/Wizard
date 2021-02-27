package application.graphics.mesh

import application.core.collection.DestroyableList
import application.core.ecs.Component
import org.lwjgl.BufferUtils
import java.nio.IntBuffer

abstract class MeshComponent(
    val vertexBuffers: DestroyableList<VertexBuffer>,
    indices: IntArray = intArrayOf()
) : Component {

    var indexBuffer: IntBuffer = BufferUtils.createIntBuffer(indices.size).apply {
        put(indices)
        flip()
    }

    abstract var vertexArrayId: Int
    abstract var indexBufferId: Int

    abstract var polygonMode: Int

    var usesCullFace = true

    companion object {
        const val ID: Short = 1
    }

    fun hasIndices(): Boolean = indexBuffer.capacity() > 0

    fun getIndexCount(): Int = indexBuffer.capacity()

    fun getVertexCount(): Int {
        var vertexCount = 0

        for (vertexBuffer in vertexBuffers) {
            vertexCount += vertexBuffer.dataBuffer.capacity()
        }

        return vertexCount
    }

    override fun getId(): Short = ID

    fun addVertexBuffer(vertexBuffer: VertexBuffer) {
        vertexBuffers.add(vertexBuffer.apply {
            vertex.attribute = vertexBuffers.size
        })
    }

    fun bind() {
        bindVertexArray()
        if (hasIndices()) {
            bindIndexBuffer()
        }
        for (vertexBuffer in vertexBuffers) {
            vertexBuffer.bind()
            vertexBuffer.unbind()
        }
        unbindVertexArray()
    }

    fun enableAttributes() {
        for (vertexBuffer in vertexBuffers) {
            vertexBuffer.vertex.enableAttribute()
        }
    }

    fun disableAttributes() {
        for (vertexBuffer in vertexBuffers) {
            vertexBuffer.vertex.disableAttribute()
        }
    }

    abstract fun bindVertexArray()
    abstract fun unbindVertexArray()
    abstract fun bindIndexBuffer()
    abstract fun unbindIndexBuffer()

    override fun onDestroy() {
        vertexBuffers.clear()
    }

}