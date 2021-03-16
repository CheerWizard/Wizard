package application.graphics.core.buffers

import application.core.Destroyable
import application.core.tools.Environment
import org.lwjgl.BufferUtils
import java.nio.IntBuffer

abstract class IndexBuffer(
    totalIndexCount: Int = Environment.INDEX_COUNT
) : Destroyable {

    abstract var id: Int

    private var currentIndexCount = 0

    var data: IntBuffer = BufferUtils.createIntBuffer(totalIndexCount)

    var isWritable = true

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

    fun writeMode() {
        isWritable = true
        data.clear()
    }

    fun readMode() {
        isWritable = false
        data.flip()
    }

    fun clear() {
        writeMode()
        currentIndexCount = 0
    }

    abstract fun prepare()
    abstract fun bind()
    abstract fun unbind()

    override fun onDestroy() {
        clear()
    }

}