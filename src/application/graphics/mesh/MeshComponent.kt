package application.graphics.mesh

import application.core.collection.DestroyableList
import application.core.ecs.Component
import application.graphics.geometry.VertexArray

open class MeshComponent(
    var vertexArray: VertexArray,
    val meshBuffers: DestroyableList<MeshBuffer> = DestroyableList()
) : Component {

    var usesCullFace = true

    companion object {
        const val ID: Short = 2
    }

    override fun getId(): Short = ID

    fun bindVertexArray() {
        vertexArray.bind()
    }

    fun unbindVertexArray() {
        vertexArray.unbind()
    }

    fun prepareMeshBuffers() {
        for (meshBuffer in meshBuffers) {
            meshBuffer.prepare()
        }
    }

    fun enableAttributes() {
        for (meshBuffer in meshBuffers) {
            meshBuffer.enableAttributes()
        }
    }

    fun disableAttributes() {
        for (meshBuffer in meshBuffers) {
            meshBuffer.disableAttributes()
        }
    }

    fun bindMeshBuffers() {
        for (meshBuffer in meshBuffers) {
            meshBuffer.bind()
        }
    }

    fun unbindMeshBuffers() {
        for (meshBuffer in meshBuffers) {
            meshBuffer.unbind()
        }
    }

    fun addMeshBuffer(meshBuffer: MeshBuffer): Int {
        meshBuffers.add(meshBuffer)
        return meshBuffers.size - 1
    }

    fun removeMeshBuffer(meshBufferId: Int) {
        meshBuffers.removeAt(meshBufferId)
    }

    fun removeMeshBuffers() {
        meshBuffers.clear()
    }

    fun getMeshBuffer(meshBufferId: Int): MeshBuffer = meshBuffers[meshBufferId]

    override fun onDestroy() {
        vertexArray.onDestroy()
        removeMeshBuffers()
    }

}