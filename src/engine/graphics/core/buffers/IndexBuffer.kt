package engine.graphics.core.buffers

import engine.core.UBuffer
import engine.core.tools.Environment

import org.lwjgl.BufferUtils
import java.nio.IntBuffer

abstract class IndexBuffer(
    var totalIndexCount: Int = Environment.INDEX_COUNT
) : UBuffer<IntBuffer>() {

    abstract var id: Int

    private var currentIndexCount = 0

    override var buffer: IntBuffer = BufferUtils.createIntBuffer(totalIndexCount)
    override var subBuffer: IntBuffer = BufferUtils.createIntBuffer(1)

    override fun allocateBuffer() {
        buffer = BufferUtils.createIntBuffer(totalIndexCount)
    }

    override fun allocateSubBuffer() {
        subBuffer = BufferUtils.createIntBuffer(1)
    }

    fun allocateIndex(indexStart: Int) {
        subBufferOffset = (indexStart * Int.SIZE_BYTES).toLong()
        subBuffer = BufferUtils.createIntBuffer(1)
    }

    fun allocateIndices(indexStart: Int, indexCount: Int) {
        subBufferOffset = (indexStart * Int.SIZE_BYTES).toLong()
        subBuffer = BufferUtils.createIntBuffer(indexCount)
    }

    fun getIndexCount(): Int = currentIndexCount

    fun add(indices: IntArray) {
        buffer.put(indices)
        currentIndexCount += indices.size
    }

    fun add(index: Int) {
        buffer.put(index)
        currentIndexCount++
    }

    fun update(indices: IntArray) {
        subBuffer.put(indices)
    }

    fun update(index: Int) {
        subBuffer.put(index)
    }

    override fun reset() {
        super.reset()
        currentIndexCount = 0
    }

}