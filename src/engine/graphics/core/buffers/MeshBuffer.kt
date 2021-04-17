package engine.graphics.core.buffers

import engine.core.Destroyable
import engine.core.tools.Environment
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
        instanceBuffer.allocateBuffer(Environment.INSTANCE_COUNT)
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
        val indices = mesh.indices
        if (indices.isUpdated) {
            indexBuffer.run {
                allocateIndices(indexStart = mesh.indexStart, indexCount = mesh.getIndexCount())
                update(indices.data)
                applyChanges()
            }
        }

        val vertices = mesh.vertices
        if (vertices.isUpdated) {
            vertexBuffer.run {
                allocateMesh(vertexStart = mesh.vertexStart, vertexCount = mesh.getVertexCount())
                update(mesh.getVerticesData())
                applyChanges()
            }
        }

        val updatedAttributes = mesh.getMeshUpdatedAttributes()
        for (updatedAttribute in updatedAttributes) {
            instanceBuffer.run {
                allocateAttribute(attributeStart = updatedAttribute.offset, attributeSize = updatedAttribute.size() * updatedAttribute.count)
                update(updatedAttribute.data)
                applyChanges()
            }
            updatedAttribute.discardChanges()
        }

        mesh.discardChanges()
    }

    fun prepare() {
        indexBuffer.prepare()
        instanceBuffer.prepare()
        vertexBuffer.prepare()
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
        instanceBuffer.unbind()
        vertexBuffer.unbind()
    }

    fun disableAttributes() {
        instanceBuffer.disableAttributes()
        vertexBuffer.disableAttributes()
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