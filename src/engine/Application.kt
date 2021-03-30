package engine

import engine.core.ecs.Engine
import org.lwjgl.glfw.GLFW

abstract class Application : Engine.Client {

    protected abstract val engine: Engine

    fun run() {
        engine.start()
    }

    protected open fun bindIOController() {
        engine.ioController.run {
            bindKeyPressedEvent(GLFW.GLFW_KEY_V) { engine.window.toggleVSync() }
            bindKeyPressedEvent(GLFW.GLFW_KEY_F) { engine.window.toggleFullScreen() }
            bindKeyPressedEvent(GLFW.GLFW_KEY_ESCAPE) { engine.window.close() }
        }
    }

    override fun onCreate() {
        println("onCreate()")
        bindIOController()
    }

    override fun onUpdate() {
    }

    override fun onDestroy() {
        println("onDestroy()")
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