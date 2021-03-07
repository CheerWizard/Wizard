package application.graphics.geometry

import application.core.Destroyable
import org.lwjgl.BufferUtils
import java.nio.IntBuffer

abstract class IndexBuffer(
        totalVertexCount: Int = MAX_VERTEX_COUNT
) : Destroyable {

    companion object {
        const val MAX_VERTEX_COUNT = 100000
    }

    abstract var id: Int

    private var currentIndexCount = 0

    var data: IntBuffer = BufferUtils.createIntBuffer(totalVertexCount * 3 / 2)

    fun capacity(): Int = data.capacity()

    fun getIndexCount(): Int = currentIndexCount

    fun setPosition(newPosition: Int) = data.position(newPosition)

    fun addIndices(indices: IntArray) {
        data.put(indices)
        currentIndexCount += indices.size
    }

    fun addIndex(index: Int) {
        data.put(index)
        currentIndexCount++
    }

    fun flip() {
        data.flip()
    }

    fun clear() {
        data.clear()
        currentIndexCount = 0
    }

    abstract fun prepare()
    abstract fun bind()
    abstract fun unbind()

    override fun onDestroy() {
        clear()
    }

}