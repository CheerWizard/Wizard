package application.graphics.geometry

import application.core.Destroyable
import org.lwjgl.BufferUtils
import java.nio.IntBuffer

abstract class IndexBuffer(capacity: Int) : Destroyable {

    companion object {
        const val DEFAULT_CAPACITY = 1024 / Int.SIZE_BYTES
    }

    abstract var id: Int

    var data: IntBuffer = BufferUtils.createIntBuffer(capacity)

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