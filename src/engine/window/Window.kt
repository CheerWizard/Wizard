package engine.window

import engine.platform.window.GLCursor
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.glViewport
import uno.glfw.GlfwWindow
import java.awt.DisplayMode
import java.awt.GraphicsEnvironment

class Window(
    private val xPosition: Int = 0,
    private val yPosition: Int = 32,
    private val display: DisplayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode,
    private val title: String,
) {

    interface Listener {
        fun onWindowResized(width: Float, height: Float)
        fun onWindowClosed()
    }

    private lateinit var mWindow: GlfwWindow

    private val cursor: Cursor = GLCursor()

    fun getWindow(): GlfwWindow = mWindow

    fun setCursorListener(cursorListener: Cursor.Listener) {
        cursor.setListener(cursorListener)
    }

    fun removeCursorListener() {
        cursor.removeListener()
    }

    fun setInputController(IOController: IOController) {
        mWindow.keyCB = { key, scancode, action, mods ->
            when (action) {
                GLFW_PRESS -> IOController.onKeyPressed(key)
                GLFW_RELEASE -> IOController.onKeyReleased(key)
            }
        }
        mWindow.mouseButtonCB = { button, action, mods ->
            when (action) {
                GLFW_PRESS -> IOController.onMousePressed(button)
                GLFW_RELEASE -> IOController.onMouseReleased(button)
            }
        }
        mWindow.scrollCB = { offset ->
            if (offset.y == -1.0) {
                IOController.onMouseScrollDown()
            } else {
                IOController.onMouseScrollUp()
            }
        }
    }

    fun setWindowListener(listener: Listener) {
        mWindow.windowCloseCB = {
            listener.onWindowClosed()
        }
        mWindow.windowSizeCB = { size ->
            listener.onWindowResized(width = size.x.toFloat(), height = size.y.toFloat())
        }
    }

    fun isOpen(): Boolean = mWindow.isOpen

    fun onCreate() {
        mWindow = GlfwWindow(width = display.width, height = display.height, title = title).apply {
            pos.x = xPosition
            pos.y = yPosition
            makeContextCurrent()
        }
        cursor.setWindow(mWindow.handle.value)
    }

    fun getRefreshRate(): Int = display.refreshRate

    fun setViewPort(
        x: Int = 0,
        y: Int = 0,
        width: Int = display.width,
        height: Int = display.height
    ) = glViewport(x, y, width, height)

    fun show() {
        mWindow.show()
    }

    private var vSyncEnabled: Int = GLFW_FALSE

    fun toggleVSync() {
        val isEnabled = if (vSyncEnabled == GLFW_TRUE) GLFW_FALSE else GLFW_TRUE
        vSyncEnabled = isEnabled
        glfwSwapInterval(isEnabled)
    }

    fun enableVSync() {
        vSyncEnabled = GLFW_TRUE
        glfwSwapInterval(vSyncEnabled)
    }

    fun disableVSync() {
        vSyncEnabled = GLFW_FALSE
        glfwSwapInterval(vSyncEnabled)
    }

    private var fullscreenEnabled = false

    fun toggleFullScreen() {
        var width = display.width
        var height = display.height

        fullscreenEnabled = !fullscreenEnabled

        if (!fullscreenEnabled) {
            width /= 2
            height /= 2
        }

        mWindow.size.x = width
        mWindow.size.y = height
    }

    fun enableFullScreen() {
        mWindow.size.x = display.width
        mWindow.size.y = display.height
    }

    fun close() {
        mWindow.shouldClose = true
    }

    fun swapBuffers() {
        mWindow.swapBuffers()
    }

    fun pollEvents() {
        glfwPollEvents()
        cursor.onUpdate()
    }

    fun onDestroy() {
        removeCursorListener()
        mWindow.destroy()
        glfwTerminate()
    }

    fun getWidth(): Float = display.width.toFloat()
    fun getHeight(): Float = display.height.toFloat()
    fun getCursorCenterX(): Float = display.width.toFloat() / 2000f
    fun getCursorCenterY(): Float = display.height.toFloat() / 2000f

}