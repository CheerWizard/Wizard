package engine.platform

import engine.Application
import engine.core.math.TransformMatrix4f
import engine.graphics.core.scene.CameraComponent
import engine.graphics.render.Render3dSystem
import org.lwjgl.glfw.GLFW

abstract class GraphicsApplication : Application() {

    protected open lateinit var cameraComponent: CameraComponent

    override fun onCreate() {
        super.onCreate()
        cameraComponent = CameraComponent(
            transformation = TransformMatrix4f(
                name = "camera"
            )
        )
    }

    override fun onWindowResized(width: Float, height: Float) {
        engine.getSystem<Render3dSystem>(Render3dSystem::class.java.simpleName).applyWindowAspectRatio(width, height)
    }

    abstract var cursorX: Float
    abstract var cursorY: Float

    override fun onCursorCoordinatesChanged(x: Float, y: Float) {
        super.onCursorCoordinatesChanged(x, y)

        if (!engine.ioController.isMouseHold()) return

        val dx = x - this.cursorX
        val dy = y - this.cursorY

        cameraComponent.rotate(x = dx, y = dy)

        this.cursorX = x
        this.cursorY = y
    }

    override fun bindIOController() {
        super.bindIOController()
        engine.ioController.run {
            bindMouseScrollDownEvent { zoomOutCamera() }
            bindMouseScrollUpEvent { zoomInCamera() }

            bindMouseHoldEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT) {}
            bindMouseReleasedEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                cursorX = engine.window.getCursorCenterX()
                cursorY = engine.window.getCursorCenterY()
            }

            bindKeyHoldEvent(GLFW.GLFW_KEY_Q) { zoomOutCamera() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_E) { zoomInCamera() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_W) { moveCameraUp() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_S) { moveCameraDown() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_A) { moveCameraLeft() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_D) { moveCameraRight() }
        }
    }

    private fun zoomOutCamera() {
        cameraComponent.zoomOut()
    }

    private fun zoomInCamera() {
        cameraComponent.zoomIn()
    }

    private fun moveCameraLeft() {
        cameraComponent.moveLeft()
    }

    private fun moveCameraRight() {
        cameraComponent.moveRight()
    }

    private fun moveCameraUp() {
        cameraComponent.moveUp()
    }

    private fun moveCameraDown() {
        cameraComponent.moveDown()
    }

}