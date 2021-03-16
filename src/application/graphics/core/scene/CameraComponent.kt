package application.graphics.core.scene

import application.core.ecs.Component
import application.core.math.TransformMatrix4f

class CameraComponent(
    var transformMatrix4f: TransformMatrix4f,
    var zoomSpeed: Float = DEFAULT_ZOOM_SPEED,
    var movementSpeed: Float = DEFAULT_MOVEMENT_SPEED,
    var rotateSpeed: Float = DEFAULT_ROTATE_SPEED
) : Component {

    override var isUpdated: Boolean = true

    companion object {
        const val ID : Short = 5
        private const val DEFAULT_MOVEMENT_SPEED = 0.1f
        private const val DEFAULT_ROTATE_SPEED = 1.5f
        private const val DEFAULT_ZOOM_SPEED = 0.25f
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

    fun zoomIn() {
        transformMatrix4f.run {
            position.z += zoomSpeed
            apply()
        }
    }

    fun zoomOut() {
        transformMatrix4f.run {
            position.z -= zoomSpeed
            apply()
        }
    }

    fun moveUp() {
        transformMatrix4f.run {
            position.y -= zoomSpeed
            apply()
        }
    }

    fun moveDown() {
        transformMatrix4f.run {
            position.y += zoomSpeed
            apply()
        }
    }

    fun moveLeft() {
        transformMatrix4f.run {
            position.x += zoomSpeed
            apply()
        }
    }

    fun moveRight() {
        transformMatrix4f.run {
            position.x -= zoomSpeed
            apply()
        }
    }

    fun rotate(x: Float, y: Float) {
        transformMatrix4f.run {
            rotation.x += y * rotateSpeed
            rotation.y += x * rotateSpeed
            apply()
        }
    }

}