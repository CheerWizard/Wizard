package application.platform

import application.Application
import application.core.math.TransformMatrix4f
import application.graphics.core.scene.CameraComponent
import application.graphics.render.Render3dSystem
import application.graphics.tools.ObjParser
import application.graphics.tools.TerrainParser
import org.lwjgl.glfw.GLFW

abstract class GraphicsApplication(title: String = "OpenGL") : Application(title = title) {

    protected val objParser: ObjParser = ObjParser()
    protected val terrainParser: TerrainParser = TerrainParser()

    protected open lateinit var cameraComponent: CameraComponent

    override fun onCreate() {
        super.onCreate()
        cameraComponent = CameraComponent(
            transformMatrix4f = TransformMatrix4f(
                name = "camera"
            )
        )
    }

    override fun onWindowResized(width: Float, height: Float) {
        engine.getSystem<Render3dSystem>(Render3dSystem.ID).applyWindowAspectRatio(width, height)
    }

    private var x = window.getCursorCenterX()
    private var y = window.getCursorCenterY()

    override fun onCursorCoordinatesChanged(x: Float, y: Float) {
        super.onCursorCoordinatesChanged(x, y)

        if (!inputController.isMouseHold()) return

        val dx = x - this.x
        val dy = y - this.y

        updateCamera {
            rotate(x = dx, y = dy)
        }

        this.x = x
        this.y = y
    }

    override fun bindInputController() {
        super.bindInputController()
        inputController.run {
            bindMouseScrollDownEvent { zoomOutCamera() }
            bindMouseScrollUpEvent { zoomInCamera() }

            bindMouseHoldEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT) {}
            bindMouseReleasedEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                x = window.getCursorCenterX()
                y = window.getCursorCenterY()
            }

            bindKeyHoldEvent(GLFW.GLFW_KEY_Q) { zoomOutCamera() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_E) { zoomInCamera() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_W) { moveCameraUp() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_S) { moveCameraDown() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_A) { moveCameraLeft() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_D) { moveCameraRight() }
        }
    }

    private fun updateCamera(updateFunction : CameraComponent.() -> Unit) {
        updateFunction.invoke(cameraComponent)
    }

    private fun zoomOutCamera() {
        updateCamera {
            zoomOut()
        }
    }

    private fun zoomInCamera() {
        updateCamera {
            zoomIn()
        }
    }

    private fun moveCameraLeft() {
        updateCamera {
            moveLeft()
        }
    }

    private fun moveCameraRight() {
        updateCamera {
            moveRight()
        }
    }

    private fun moveCameraUp() {
        updateCamera {
            moveUp()
        }
    }

    private fun moveCameraDown() {
        updateCamera {
            moveDown()
        }
    }

}