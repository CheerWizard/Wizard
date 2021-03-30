package engine.graphics.core.scene

import engine.core.ecs.Component
import engine.core.math.TransformMatrix4f
import engine.core.tools.FpsTicker

class CameraComponent(
    var transformation: TransformMatrix4f = TransformMatrix4f(),
    var zoomSpeed: Float = DEFAULT_ZOOM_SPEED,
    var movementSpeed: Float = DEFAULT_MOVEMENT_SPEED,
    var rotateSpeed: Float = DEFAULT_ROTATE_SPEED
) : Component {

    companion object {
        const val ID : Short = 5
        private const val DEFAULT_MOVEMENT_SPEED = 10f
        private const val DEFAULT_ROTATE_SPEED = 240f
        private const val DEFAULT_ZOOM_SPEED = 10f
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

    fun zoomIn() {
        val deltaTime = FpsTicker.getDeltaTimeSeconds()
        transformation.run {
            position.z += zoomSpeed * deltaTime
            applyChanges()
        }
    }

    fun zoomOut() {
        val deltaTime = FpsTicker.getDeltaTimeSeconds()
        transformation.run {
            position.z -= zoomSpeed * deltaTime
            applyChanges()
        }
    }

    fun moveUp() {
        val deltaTime = FpsTicker.getDeltaTimeSeconds()
        transformation.run {
            position.y -= zoomSpeed * deltaTime
            applyChanges()
        }
    }

    fun moveDown() {
        val deltaTime = FpsTicker.getDeltaTimeSeconds()
        transformation.run {
            position.y += zoomSpeed * deltaTime
            applyChanges()
        }
    }

    fun moveLeft() {
        val deltaTime = FpsTicker.getDeltaTimeSeconds()
        transformation.run {
            position.x += zoomSpeed * deltaTime
            applyChanges()
        }
    }

    fun moveRight() {
        val deltaTime = FpsTicker.getDeltaTimeSeconds()
        transformation.run {
            position.x -= zoomSpeed * deltaTime
            applyChanges()
        }
    }

    fun rotate(x: Float, y: Float) {
        val deltaTime = FpsTicker.getDeltaTimeSeconds()
        transformation.run {
            rotation.x += y * rotateSpeed * deltaTime
            rotation.y += x * rotateSpeed * deltaTime
            applyChanges()
        }
    }

}