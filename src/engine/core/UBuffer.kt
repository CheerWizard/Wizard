package engine.core

import java.nio.Buffer

abstract class UBuffer<T : Buffer> : Destroyable {

    abstract var buffer: T
    abstract var subBuffer: T

    protected var bufferWritable = true

    protected var isBound = false

    protected var subBufferOffset: Long = 0 // pointer measured in bytes.

    abstract fun allocateBuffer()
    abstract fun allocateSubBuffer()

    fun prepare() {
        bind()
        onPrepare()
    }

    protected abstract fun onPrepare()

    fun bind() {
        if (!isBound) {
            isBound = true
            onBind()
        }
    }

    protected abstract fun onBind()

    fun applyChanges() {
        readSubBuffer()
        bind()
        onUpdate()
    }

    protected abstract fun onUpdate()

    fun unbind() {
        if (isBound) {
            isBound = false
            onUnbind()
        }
    }

    protected abstract fun onUnbind()

    fun capacity(): Int = buffer.capacity()

    fun position(): Int = buffer.position()

    fun setPosition(newPosition: Int) = buffer.position(newPosition)

    fun writeSubBuffer() {
        subBuffer.rewind()
    }

    fun readSubBuffer() {
        subBuffer.flip()
    }

    fun writeBuffer() {
        if (!bufferWritable) {
            bufferWritable = true
            buffer.rewind()
        }
    }

    fun readBuffer() {
        if (bufferWritable) {
            bufferWritable = false
            buffer.flip()
        }
    }

    open fun reset() {
        buffer.clear()
        subBuffer.clear()
        bufferWritable = true
        isBound = false
    }

    override fun onDestroy() {
        reset()
    }
    
}