package application.graphics.geometry

import application.core.Destroyable
import org.joml.Matrix4f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

abstract class VertexBuffer(
    totalMeshCount: Int = DEFAULT_TOTAL_MESH_COUNT,
    averageMeshSize: Int = DEFAULT_AVERAGE_MESH_SIZE
) : Destroyable {

    companion object {
        const val DEFAULT_TOTAL_MESH_COUNT = 100
        const val DEFAULT_AVERAGE_MESH_SIZE = Mesh.MAX_SIZE
    }

    abstract var id: Int

    abstract var polygonMode: Int

    protected val attributes = ArrayList<Attribute>()
    protected val meshes = ArrayList<Mesh>(totalMeshCount)

    val data: FloatBuffer = BufferUtils.createFloatBuffer(totalMeshCount * averageMeshSize)

    fun capacity(): Int = data.capacity()

    fun position(): Int = data.position()

    fun setPosition(newPosition: Int) = data.position(newPosition)

    fun addAttribute(attribute: Attribute) {
        attributes.add(attribute)
    }

    fun addAttributes(attributes: Array<Attribute>) {
        this.attributes.addAll(attributes)
    }

    fun getAttributes(): List<Attribute> = attributes

    fun removeAttributes() {
        attributes.clear()
    }

    abstract fun enableAttributes()
    abstract fun disableAttributes()

    fun addVertex(vertex: Vertex) {
        data.run {
            put(vertex.position.x)
            put(vertex.position.y)
            put(vertex.position.z)

            put(vertex.color.x)
            put(vertex.color.y)
            put(vertex.color.z)
            put(vertex.color.w)

            put(vertex.textureCoordinate.x)
            put(vertex.textureCoordinate.y)

            put(vertex.normal.x)
            put(vertex.normal.y)
            put(vertex.normal.z)

            put(vertex.textureSlots)
        }
    }

    private val vertexPosition = Vector4f()

    private fun resetVertexPosition() {
        vertexPosition.x = 0f
        vertexPosition.y = 0f
        vertexPosition.z = 0f
        vertexPosition.w = 1f
    }

    fun transformMesh(meshId: Int, matrix4f: Matrix4f) {
        var meshStart = 0
        for (i in 0 until meshId) {
            meshStart += meshes[i].size()
        }

        val meshSize = meshes[meshId].size()
        val vertexSize = meshes[meshId].vertexSize

        resetVertexPosition()

        var vertexId = meshStart
        while (vertexId < meshSize) {
            val x = data.get(vertexId)
            val y = data.get(vertexId + 1)
            val z = data.get(vertexId + 2)

            vertexPosition.x = x
            vertexPosition.y = y
            vertexPosition.z = z

            vertexPosition.mul(matrix4f)

            data.put(vertexId, vertexPosition.x)
            data.put(vertexId + 1, vertexPosition.y)
            data.put(vertexId + 2, vertexPosition.z)

            resetVertexPosition()

            vertexId += vertexSize
        }
    }

    fun addVertex(vertexArray: FloatArray) {
        data.put(vertexArray)
    }

    fun flip() {
        data.flip()
    }

    fun clear() {
        data.clear()
    }

    abstract fun prepare()
    abstract fun bind()
    abstract fun unbind()

    override fun onDestroy() {
        removeAttributes()
        clear()
    }

}