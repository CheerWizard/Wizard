package application.graphics.mesh

import application.core.Destroyable
import application.core.collection.Vector3fList
import application.core.math.Vector3f
import application.graphics.geometry.IndexBuffer
import application.graphics.geometry.VertexBuffer
import org.joml.Matrix4f

class MeshBuffer(
        var positionBuffer: VertexBuffer,
        var indexBuffer: IndexBuffer,
        var normalsBuffer: VertexBuffer? = null,
) : Destroyable {

    private val meshList = ArrayList<Mesh>()

    fun getIndexCount(): Int = indexBuffer.getIndexCount()

    fun addPosition(position: Vector3f) : Int = positionBuffer.add(position)

    fun addMesh(positions: Vector3fList, indices: IntArray, normals: FloatArray): Int {
        val meshId = addMesh(positions = positions, indices = indices)

        normalsBuffer?.add(normals)

        return meshId
    }

    fun addMesh(positions: Vector3fList, indices: IntArray) : Int {
        val vertexIds = IntArray(positions.size)

        for (i in indices.indices) {
            val vertexCount = positionBuffer.getVertexCount()
            indexBuffer.addIndex(indices[i] + vertexCount)
        }

        for (i in positions.indices) {
            vertexIds[i] = addPosition(positions[i])
        }

        meshList.add(Mesh(
                vertexIds = vertexIds,
                indexCount = indices
        ))

        return meshList.size - 1
    }

    fun transformMesh(meshId: Int, matrix4f: Matrix4f) {
        val vertexIds = meshList[meshId].vertexIds
        for (vertexId in vertexIds) {
            positionBuffer.transformVertex(vertexId = vertexId, matrix4f = matrix4f)
        }
    }

    fun prepare() {
        indexBuffer.prepare()
        positionBuffer.prepare()
        normalsBuffer?.prepare()
    }

    fun enableAttributes() {
        positionBuffer.enableAttributes()
        normalsBuffer?.enableAttributes()
    }

    fun bind() {
        indexBuffer.bind()
        positionBuffer.bind()
        normalsBuffer?.bind()
    }

    fun unbind() {
        normalsBuffer?.unbind()
        positionBuffer.unbind()
        indexBuffer.unbind()
    }

    fun disableAttributes() {
        positionBuffer.disableAttributes()
        normalsBuffer?.disableAttributes()
    }

    override fun onDestroy() {
        indexBuffer.onDestroy()
        positionBuffer.onDestroy()
        normalsBuffer?.onDestroy()
    }

}