package engine.window

import engine.core.Destroyable
import engine.graphics.math.Color4f
import engine.io.IOController
import engine.platform.window.GLCursor
import org.joml.Vector2i
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil.NULL
import java.awt.DisplayMode
import java.awt.GraphicsEnvironment

open class Window(
    private val display: DisplayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode,
    val title: String,
) : Destroyable {

    interface Listener {
        fun onWindowResized(width: Float, height: Float)
        fun onWindowClosed()
    }

    protected var pointer: Long = 0

    private val cursor: Cursor = GLCursor()

    open val ioController : IOController = IOController()

    var backgroundColor : Color4f = Color4f()

    fun setCursorListener(cursorListener: Cursor.Listener) {
        cursor.setListener(cursorListener)
    }

    fun removeCursorListener() {
        cursor.removeListener()
    }

    fun setWindowListener(listener: Listener) {
        glfwSetWindowCloseCallback(pointer) {
            listener.onWindowClosed()
        }
        glfwSetWindowSizeCallback(pointer) { _, width, height ->
            setViewport()
            listener.onWindowResized(width = width.toFloat(), height = height.toFloat())
        }
    }

    fun isOpen(): Boolean = !glfwWindowShouldClose(pointer)

    open fun onCreate() {
        pointer = glfwCreateWindow(
            display.width,
            display.height,
            title,
            NULL,
            NULL
        )

        cursor.setWindow(pointer)

        makeContextCurrent()
        createIOController()
    }

    protected open fun createIOController() {
        ioController.run {
            onCreate()
            onBind()
        }

        glfwSetKeyCallback(pointer) { _, key, _, action, _ ->
            when (action) {
                GLFW_PRESS -> ioController.onKeyPressed(key)
                GLFW_RELEASE -> ioController.onKeyReleased(key)
            }
        }
        glfwSetMouseButtonCallback(pointer) { _, button, action, _ ->
            when (action) {
                GLFW_PRESS -> ioController.onMousePressed(button)
                GLFW_RELEASE -> ioController.onMouseReleased(button)
            }
        }
        glfwSetScrollCallback(pointer) { _, xOffset, yOffset ->
            ioController.onMouseScrolled(xOffset = xOffset, yOffset = yOffset)
            if (yOffset == -1.0) {
                ioController.onMouseScrollDown()
            } else {
                ioController.onMouseScrollUp()
            }
        }
    }

    open fun makeContextCurrent() {
        glfwMakeContextCurrent(pointer)
        GL.createCapabilities()
    }

    fun getRefreshRate(): Int = display.refreshRate

    fun setMonitor(monitor: Monitor) {
        glfwSetWindowMonitor(
            pointer,
            monitor.pointer,
            monitor.position.x,
            monitor.position.y,
            monitor.size.x,
            monitor.size.y,
            monitor.refreshRate
        )
    }

    private val frameBufferWidth = BufferUtils.createIntBuffer(1)
    private val frameBufferHeight = BufferUtils.createIntBuffer(1)

    fun getFrameBufferSize() : Vector2i {
        val frameBufferSize = Vector2i()

        glfwGetFramebufferSize(pointer, frameBufferWidth, frameBufferHeight)

        return frameBufferSize.apply {
            x = frameBufferWidth.get(0)
            y = frameBufferHeight.get(0)
        }
    }

    fun setViewport(
        x: Int = 0,
        y: Int = 0,
        width: Int,
        height: Int
    ) = glViewport(x, y, width, height)

    fun setViewport(
        x: Int = 0,
        y: Int = 0
    ) {
        val frameBufferSize = getFrameBufferSize()
        glViewport(x, y, frameBufferSize.x, frameBufferSize.y)
    }

    fun show() = glfwShowWindow(pointer)

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

        setSize(width, height)
    }

    fun enableFullScreen() {
        setSize(display.width, display.height)
    }

    fun close() {
        glfwSetWindowShouldClose(pointer, true)
    }

    fun swapBuffers() {
        glfwSwapBuffers(pointer)
    }

    fun pollEvents() {
        glfwPollEvents()
        cursor.onUpdate()
        ioController.onUpdate()
    }

    fun onUpdate() {
        swapBuffers()
        clearDisplay()
        pollEvents()
    }

    fun clearDisplay() {
        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w)
        glClear(GL30.GL_COLOR_BUFFER_BIT or GL30.GL_DEPTH_BUFFER_BIT)
    }

    override fun onDestroy() {
        removeCursorListener()
        ioController.onDestroy()
        glfwDestroyWindow(pointer)
        glfwTerminate()
    }

    private val windowBufferWidth = BufferUtils.createIntBuffer(1)
    private val windowBufferHeight = BufferUtils.createIntBuffer(1)

    fun requestSize() = glfwGetWindowSize(pointer, windowBufferWidth, windowBufferHeight)

    fun getWidth(): Float = windowBufferWidth.get(0).toFloat()

    fun getHeight(): Float = windowBufferHeight.get(0).toFloat()

    fun getCursorCenterX(): Float = getWidth() / 2000f

    fun getCursorCenterY(): Float = getHeight() / 2000f

    fun hide() = glfwHideWindow(pointer)

    fun isFocused(): Boolean = glfwGetWindowAttrib(pointer, GLFW_FOCUSED) == GLFW_FOCUSED

    fun isMouseHold(): Boolean = ioController.isMouseHold()

    fun setSize(width: Int, height: Int) {
        glfwSetWindowSize(pointer, width, height)
    }

    fun setResizable() {
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
    }

    fun getSize(): Vector2i {
        requestSize()
        return Vector2i(windowBufferWidth.get(), windowBufferHeight.get())
    }

}