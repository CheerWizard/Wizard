package application

import application.core.ecs.Engine
import application.window.Cursor
import application.window.InputController
import application.window.Window
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback

abstract class Application(title: String) :
    Engine.Listener,
    Window.Listener,
    Cursor.Listener
{

    protected abstract val engine: Engine

    protected val window: Window = Window(title = title)
    protected val inputController = InputController()

    fun run() {
        loadLibraries()
        engine.start()
    }

    private fun loadLibraries() {
        println("Loading libraries...")

        System.setProperty("java.awt.headless", "true")
        GLFWErrorCallback.createPrint().set()
        check(GLFW.glfwInit()) { "Unable to initialize GLFW!" }

        println("Libraries loaded successfully!")
    }

    protected open fun bindInputController() {
        inputController.run {
            bindKeyPressedEvent(GLFW.GLFW_KEY_V) { window.toggleVSync() }
            bindKeyPressedEvent(GLFW.GLFW_KEY_F) { window.toggleFullScreen() }
            bindKeyPressedEvent(GLFW.GLFW_KEY_ESCAPE) { window.close() }
        }
    }

    override fun createWindow() {
        bindInputController()

        window.run {
            onCreate()
            setInputController(inputController)
            setWindowListener(this@Application)
            setCursorListener(this@Application)
        }
    }

    override fun isOpen(): Boolean = window.isOpen()
    override fun getHeight(): Float = window.getHeight()
    override fun getWidth(): Float = window.getWidth()

    override fun onCreate() {
        println("onCreate()")
    }

    override fun onUpdate() {
        window.onUpdate()
        inputController.onUpdate()
    }

    override fun onDestroy() {
        println("onDestroy()")
        window.onDestroy()
        inputController.onDestroy()
    }

    override fun onWindowClosed() {
        println("onWindowClosed()")
    }

    override fun onCursorCoordinatesChanged(x: Float, y: Float) {
        println("Cursor [x : $x] [y : $y]")
    }

    override fun onWindowResized(width: Float, height: Float) {
        println("onWindowResized()")
    }

}