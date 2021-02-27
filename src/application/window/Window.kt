package application.window

import application.platform.window.GLCursor
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryUtil.NULL
import java.awt.DisplayMode
import java.awt.GraphicsEnvironment

class Window(
    private val xPosition: Int = 0,
    private val yPosition: Int = 32,
    private val display: DisplayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode,
    private val title: CharSequence,
) {

    interface Listener {
        fun onWindowResized(width: Float, height: Float)
        fun onWindowClosed()
    }

    private var id: Long = 0

    private val cursor = GLCursor()

    fun setCursorListener(cursorListener: Cursor.Listener) {
        cursor.setListener(cursorListener)
    }

    fun removeCursorListener() {
        cursor.removeListener()
    }

    fun setInputController(inputController: InputController) {
        glfwSetKeyCallback(id) { window, key, scancode, action, mods ->
            when (action) {
                GLFW_PRESS -> inputController.onKeyPressed(key)
                GLFW_RELEASE -> inputController.onKeyReleased(key)
            }
        }
        glfwSetMouseButtonCallback(id) { window, button, action, mods ->
            when (action) {
                GLFW_PRESS -> inputController.onMousePressed(button)
                GLFW_RELEASE -> inputController.onMouseReleased(button)
            }
        }
        glfwSetScrollCallback(id) { window, xoffset, yoffset ->
            if (yoffset == -1.0) {
                inputController.onMouseScrollDown()
            } else {
                inputController.onMouseScrollUp()
            }
        }
    }

    fun setWindowListener(listener: Listener) {
        glfwSetWindowCloseCallback(id) {
            listener.onWindowClosed()
        }
        glfwSetWindowSizeCallback(id) { window, width, height ->
            listener.onWindowResized(width = width.toFloat(), height = height.toFloat())
        }
    }

    fun isOpen(): Boolean = !glfwWindowShouldClose(id)

    fun onCreate() {
        id = glfwCreateWindow(display.width, display.height, title, NULL, NULL)
        if (id == NULL) throw RuntimeException("Failed to create the GLFW window")

        glfwSetWindowPos(id, xPosition, yPosition)
        glfwMakeContextCurrent(id)
        cursor.setWindow(id)
        glfwShowWindow(id)

        toggleVSync()
    }

    private var vSyncEnabled: Int = GLFW_FALSE

    fun toggleVSync() {
        val enable = if (vSyncEnabled == GLFW_TRUE) GLFW_FALSE else GLFW_TRUE
        glfwSwapInterval(enable)
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

        glfwSetWindowSize(id, width, height)
    }

    fun close() {
        glfwSetWindowShouldClose(id, true)
    }

    fun onUpdate() {
        glfwSwapBuffers(id)
        glfwPollEvents()
        cursor.onUpdate()
    }

    fun onDestroy() {
        removeCursorListener()
        glfwFreeCallbacks(id)
        glfwDestroyWindow(id)
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    fun getWidth(): Float = display.width.toFloat()
    fun getHeight(): Float = display.height.toFloat()
    fun getCursorCenterX(): Float = display.width.toFloat() / 2000f
    fun getCursorCenterY(): Float = display.height.toFloat() / 2000f

}