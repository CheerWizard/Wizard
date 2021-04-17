package engine

import engine.core.ecs.Engine

abstract class Application : Engine.Client {

    protected abstract val engine: Engine

    fun run() {
        engine.run()
    }

    override fun onCreate() {
        println("onCreate()")
    }

    override fun onBindIOController() {
        println("onBindIOController()")
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