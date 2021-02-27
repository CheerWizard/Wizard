package application.graphics.component

import application.core.ecs.Component
import application.core.math.TransformMatrix4f

class CameraComponent(
    var transformMatrix4f: TransformMatrix4f,
    var zoomSpeed: Float = DEFAULT_ZOOM_SPEED,
    var movementSpeed: Float = DEFAULT_MOVEMENT_SPEED,
    var rotateSpeed: Float = DEFAULT_ROTATE_SPEED
) : Component {

    companion object {
        const val ID : Short = 4
        private const val DEFAULT_MOVEMENT_SPEED = 0.1f
        private const val DEFAULT_ROTATE_SPEED = 1.5f
        private const val DEFAULT_ZOOM_SPEED = 0.25f
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

    fun zoomIn() {
        transformMatrix4f.position.z += zoomSpeed
        transformMatrix4f.apply()
    }

    fun zoomOut() {
        transformMatrix4f.position.z -= zoomSpeed
        transformMatrix4f.apply()
    }

    fun moveUp() {
        transformMatrix4f.position.minusY(movementSpeed)
        transformMatrix4f.apply()
    }

    fun moveDown() {
        transformMatrix4f.position.addY(movementSpeed)
        transformMatrix4f.apply()
    }

    fun moveLeft() {
        transformMatrix4f.position.addX(movementSpeed)
        transformMatrix4f.apply()
    }

    fun moveRight() {
        transformMatrix4f.position.minusX(movementSpeed)
        transformMatrix4f.apply()
    }

    fun rotate(x: Float, y: Float) {
        transformMatrix4f.rotation.addX(y * rotateSpeed)
        transformMatrix4f.rotation.addY(x * rotateSpeed)
        transformMatrix4f.apply()
    }

}