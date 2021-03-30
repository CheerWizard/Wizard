package engine.window

import org.lwjgl.BufferUtils

abstract class Cursor {

    interface Listener {
        fun onCursorCoordinatesChanged(x: Float, y: Float)
    }

    protected val xCoordinates = BufferUtils.createDoubleBuffer(1)
    protected val yCoordinates = BufferUtils.createDoubleBuffer(1)

    protected var windowId: Long = 0

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun removeListener() {
        this.listener = null
    }

    fun setWindow(windowId: Long) {
        this.windowId = windowId
    }

    fun onUpdate() {
        listener?.let { l ->
            updateCoordinates()
            l.onCursorCoordinatesChanged(xCoordinates.get(0).toFloat() / 1000f, yCoordinates.get(0).toFloat() / 1000f)
        }
    }

    abstract fun updateCoordinates()

}