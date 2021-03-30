package engine.graphics.math

import org.joml.Vector3f

class Rotator3f(
    x: Float = 0f,
    y: Float = 0f,
    z: Float = 0f
) : Vector3f(
    x,
    y,
    z
) {

    fun setXDegree(xDegree: Double) {
        x = Math.toRadians(xDegree).toFloat()
    }

    fun setYDegree(yDegree: Double) {
        y = Math.toRadians(yDegree).toFloat()
    }

    fun setZDegree(zDegree: Double) {
        z = Math.toRadians(zDegree).toFloat()
    }

    fun addXDegree(xDegree: Double) {
        x += Math.toRadians(xDegree).toFloat()
    }

    fun addYDegree(yDegree: Double) {
        y += Math.toRadians(yDegree).toFloat()
    }

    fun addZDegree(zDegree: Double) {
        z += Math.toRadians(zDegree).toFloat()
    }

}