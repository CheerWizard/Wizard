package engine.graphics.core.buffers

import engine.core.Destroyable
import engine.graphics.core.mesh.Mesh
import engine.graphics.shader.attributes.AttributeList

class MeshBuffer(
    var indexBuffer: IndexBuffer,
    var vertexBuffer: VertexBuffer,
    var instanceBuffer: VertexBuffer
) : Destroyable {

    fun getIndexCount(): Int = indexBuffer.getIndexCount()

    fun getVertexCount(): Int = vertexBuffer.getVertexCount()

    fun allocateBuffer() {
        allocateIndexBuffer()
        allocateVertexBuffer()
        allocateInstanceBuffer()
    }

    fun allocateIndexBuffer() {
        indexBuffer.allocateBuffer()
    }

    fun allocateVertexBuffer() {
        vertexBuffer.allocateBuffer()
    }

    fun allocateInstanceBuffer() {
        instanceBuffer.allocateBuffer()
    }

    fun addMesh(mesh: Mesh) {
        mesh.vertexStart = getVertexCount()
        mesh.indexStart = getIndexCount()

        addIndices(mesh.indices.data)

        vertexBuffer.run {
            addVertexCount(mesh.getVertexCount())
            add(mesh.getVerticesData())
        }

        instanceBuffer.add(mesh.getMeshData())
    }

    private fun addIndices(indices: IntArray) {
        val vertexCount = getVertexCount()
        for (i in indices.indices) {
            indexBuffer.add(indices[i] + vertexCount)
        }
    }

    fun tryUpdateMesh(mesh: Mesh) {
        mesh.run {
            if (indices.isUpdated) {
                indexBuffer.run {
                    allocateIndices(indexStart = mesh.indexStart, indexCount = mesh.getIndexCount())
                    update(indices.data)
                    applyChanges()
                }
            }

            if (vertices.isUpdated) {
                vertexBuffer.run {
                    allocateMesh(vertexStart = mesh.vertexStart, vertexCount = mesh.getVertexCount())
                    update(getVerticesData())
                    applyChanges()
                }
            }

            val updatedAttributes = mesh.getMeshUpdatedAttributes()
            for (updatedAttribute in updatedAttributes) {
                instanceBuffer.run {
                    allocateAttribute(attributeStart = updatedAttribute.offset, attributeSize = updatedAttribute.size())
                    update(updatedAttribute.data)
                    applyChanges()
                }
                updatedAttribute.discardChanges()
            }

            discardChanges()
        }
    }

    fun prepare() {
        indexBuffer.prepare()
        vertexBuffer.prepare()
        instanceBuffer.prepare()
    }

    fun enableAttributes() {
        vertexBuffer.enableAttributes()
        instanceBuffer.enableAttributes()
    }

    fun bind() {
        indexBuffer.bind()
        vertexBuffer.bind()
        instanceBuffer.bind()
    }

    fun unbind() {
        indexBuffer.unbind()
        vertexBuffer.unbind()
        instanceBuffer.unbind()
    }

    fun disableAttributes() {
        vertexBuffer.disableAttributes()
        instanceBuffer.disableAttributes()
    }

    fun readBuffers() {
        indexBuffer.readBuffer()
        vertexBuffer.readBuffer()
        instanceBuffer.readBuffer()
    }

    fun writeBuffers() {
        indexBuffer.writeBuffer()
        vertexBuffer.writeBuffer()
        instanceBuffer.writeBuffer()
    }

    fun readSubBuffers() {
        indexBuffer.readSubBuffer()
        vertexBuffer.readSubBuffer()
        instanceBuffer.readSubBuffer()
    }

    fun writeSubBuffers() {
        indexBuffer.writeSubBuffer()
        vertexBuffer.writeSubBuffer()
        instanceBuffer.writeSubBuffer()
    }

    override fun onDestroy() {
        indexBuffer.onDestroy()
        vertexBuffer.onDestroy()
        instanceBuffer.onDestroy()
    }

    fun getAttributeList(): AttributeList = AttributeList().apply {
        addAll(vertexBuffer.attributes)
        addAll(instanceBuffer.attributes)
    }

}