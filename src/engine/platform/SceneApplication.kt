package engine.platform

import engine.Application
import engine.core.math.TransformMatrix4f
import engine.graphics.core.scene.CameraComponent
import engine.graphics.render.Render3dSystem
import engine.window.Window
import org.lwjgl.glfw.GLFW

abstract class SceneApplication : Application() {

    protected open lateinit var cameraComponent: CameraComponent

    protected lateinit var sceneWindow : Window

    private var cursorX: Float = 0f
    private var cursorY: Float = 0f

    override fun onCreate() {
        super.onCreate()
        sceneWindow = engine.createWindow("Scene")

        cursorX = sceneWindow.getCursorCenterX()
        cursorY = sceneWindow.getCursorCenterY()

        cameraComponent = CameraComponent(
            transformation = TransformMatrix4f(
                name = "camera"
            )
        )

        sceneWindow.makeContextCurrent()
    }

    override fun onWindowResized(width: Float, height: Float) {
        engine.getSystem<Render3dSystem>(Render3dSystem::class.java.simpleName).applyWindowAspectRatio(width, height)
    }

    override fun onCursorCoordinatesChanged(x: Float, y: Float) {
        super.onCursorCoordinatesChanged(x, y)

        if (sceneWindow.isFocused() && sceneWindow.isMouseHold()) {
            val dx = x - this.cursorX
            val dy = y - this.cursorY

            cameraComponent.rotate(x = dx, y = dy)

            this.cursorX = x
            this.cursorY = y
        }
    }

    override fun onBindIOController() {
        super.onBindIOController()

        sceneWindow.ioController.run {
            bindKeyPressedEvent(GLFW.GLFW_KEY_V) { sceneWindow.toggleVSync() }
            bindKeyPressedEvent(GLFW.GLFW_KEY_ESCAPE) { sceneWindow.close() }

            bindMouseScrollDownEvent { zoomOutCamera() }
            bindMouseScrollUpEvent { zoomInCamera() }

            bindMouseHoldEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT) {}
            bindMouseReleasedEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                cursorX = sceneWindow.getCursorCenterX()
                cursorY = sceneWindow.getCursorCenterY()
            }

            bindKeyHoldEvent(GLFW.GLFW_KEY_Q) { zoomOutCamera() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_E) { zoomInCamera() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_W) { moveCameraUp() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_S) { moveCameraDown() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_A) { moveCameraLeft() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_D) { moveCameraRight() }
        }

        sceneWindow.run {
            setViewport()
            disableVSync()
            setWindowListener(this@SceneApplication)
            setCursorListener(this@SceneApplication)
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

    override fun createMaxFps(): Long = sceneWindow.getRefreshRate().toLong()

    override fun isOpen(): Boolean = sceneWindow.isOpen()

}