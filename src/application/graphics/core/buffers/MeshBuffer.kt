package application.graphics.core.buffers

import application.core.Destroyable
import application.graphics.core.mesh.Mesh
import application.graphics.shader.Attribute
import org.joml.Vector3f

abstract class MeshBuffer(
    var indexBuffer: IndexBuffer,
    var positionBuffer: VertexBuffer? = null,
    var normalsBuffer: VertexBuffer? = null,
) : Destroyable {

    abstract fun createPositionBuffer(name: String)
    abstract fun createNormalBuffer(name: String)

    fun getIndexCount(): Int = indexBuffer.getIndexCount()

    fun getVertexCount(): Int {
        positionBuffer?.let {
            return it.getVertexCount()
        }
        return 0
    }

    fun addMesh(mesh: Mesh) {
        addIndices(mesh.indices)
        addPositions(mesh.positions)

        val normals = mesh.normals
        if (normals != null) {
            addNormals(normals)
        }
    }

    fun addNormals(normals: FloatArray) = normalsBuffer?.add(normals)

    fun addPositions(positions: FloatArray) = positionBuffer?.add(positions)

    fun addPosition(position: Vector3f) = positionBuffer?.add(position)

    private fun addIndices(indices: IntArray) {
        val vertexCount = getVertexCount()
        for (i in indices.indices) {
            indexBuffer.addIndex(indices[i] + vertexCount)
        }
    }

    fun prepare() {
        indexBuffer.prepare()
        positionBuffer?.prepare()
        normalsBuffer?.prepare()
    }

    fun enableAttributes() {
        positionBuffer?.enableAttributes()
        normalsBuffer?.enableAttributes()
    }

    fun bind() {
        indexBuffer.bind()
        positionBuffer?.bind()
        normalsBuffer?.bind()
    }

    fun unbind() {
        indexBuffer.unbind()
        normalsBuffer?.unbind()
        positionBuffer?.unbind()
    }

    fun disableAttributes() {
        positionBuffer?.disableAttributes()
        normalsBuffer?.disableAttributes()
    }

    fun readMode() {
        indexBuffer.readMode()
        positionBuffer?.readMode()
        normalsBuffer?.readMode()
    }

    fun writeMode() {
        indexBuffer.writeMode()
        positionBuffer?.writeMode()
        normalsBuffer?.writeMode()
    }

    override fun onDestroy() {
        indexBuffer.onDestroy()
        positionBuffer?.onDestroy()
        normalsBuffer?.onDestroy()
    }

    fun getAttributes(): ArrayList<Attribute> {
        return ArrayList<Attribute>().apply {
            positionBuffer?.let {
                add(it.attribute)
            }
            normalsBuffer?.let {
                add(it.attribute)
            }
        }
    }

}