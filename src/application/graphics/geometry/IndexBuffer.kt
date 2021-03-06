package application.graphics.geometry

import application.core.Destroyable
import org.lwjgl.BufferUtils
import java.nio.IntBuffer

abstract class IndexBuffer(
    totalMeshCount: Int = DEFAULT_TOTAL_MESH_COUNT,
    averageMeshIndexCount: Int = AVERAGE_MESH_INDEX_COUNT
) : Destroyable {

    companion object {
        const val DEFAULT_TOTAL_MESH_COUNT = 100
        const val AVERAGE_MESH_INDEX_COUNT = Mesh.MAX_SIZE
    }

    abstract var id: Int

    var data: IntBuffer = BufferUtils.createIntBuffer(totalMeshCount * averageMeshIndexCount)

    fun capacity(): Int = data.capacity()

    fun position(): Int = data.position()

    fun setPosition(newPosition: Int) = data.position(newPosition)

    fun addIndex(index: Int) {
        data.put(index)
    }

    fun addIndex(indexArray: IntArray) {
        data.put(indexArray)
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
        clear()
    }

}