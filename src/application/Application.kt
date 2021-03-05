package application

import application.core.ecs.Engine
import application.window.Cursor
import application.window.InputController
import application.window.Window
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback

abstract class Application(title: String) :
    Engine.Editor,
    Window.Listener,
    Cursor.Listener
{

    protected val window: Window = Window(title = title)
    protected abstract val engine: Engine

    protected val inputController = InputController()

    fun run() {
        loadLibraries()
        engine.start()
    }

    private fun loadLibraries() {
        System.setProperty("java.awt.headless", "true")
        GLFWErrorCallback.createPrint().set()
        check(GLFW.glfwInit()) { "Unable to initialize GLFW!" }
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

}