package application.core.math

import org.joml.Math
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer
import kotlin.math.sqrt

open class Vector3f(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f
) {

    companion object {
        private val BUFFER = BufferUtils.createFloatBuffer(3)
    }

    fun size(): Int = 3

    fun byteSize(): Int = size() * Float.SIZE_BYTES

    fun squaredLength(): Float = x * x + y * y + z * z

    fun length(): Float = sqrt(squaredLength().toDouble()).toFloat()

    fun normalize() = divide(length())

    fun add(other: Vector3f): Vector3f {
        x += other.x
        y += other.y
        z += other.z
        return this
    }

    open fun addX(x: Float) {
        this.x += x
    }

    open fun addY(y: Float) {
        this.y += y
    }

    open fun addZ(z: Float) {
        this.z += z
    }

    fun minus(other: Vector3f) {
        other.negate()
        add(other)
    }

    open fun minusX(x: Float) {
        this.x += -x
    }

    open fun minusY(y: Float) {
        this.y += -y
    }

    open fun minusZ(z: Float) {
        this.z += -z
    }

    fun negate() : Vector3f = scale(-1f)

    fun scale(scalar: Float): Vector3f {
        x *= scalar
        y *= scalar
        z *= scalar
        return this
    }

    fun scaleX(scalar: Float) {
        x *= scalar
    }

    fun scaleY(scalar: Float) {
        y *= scalar
    }

    fun scaleZ(scalar: Float) {
        z *= scalar
    }

    fun divide(scalar: Float) = scale(1f / scalar)

    fun multiply(other: Vector3f): Float = x * other.x + y * other.y + z * other.z

    fun cross(other: Vector3f): Vector3f {
        val x = y * other.z - z * other.y
        val y = z * other.x - this.x * other.z
        val z = this.x * other.y - this.y * other.x
        return Vector3f(x, y, z)
    }

    fun cross(v: Vector3f, dest: Vector3f): Vector3f {
        val rx = Math.fma(y, v.z, -z * v.y)
        val ry = Math.fma(z, v.x, -x * v.z)
        val rz = Math.fma(x, v.y, -y * v.x)
        dest.x = rx
        dest.y = ry
        dest.z = rz
        return dest
    }

    fun lerp(other: Vector3f, alpha: Float) {
        scale(1f - alpha)
        other.scale(alpha)
        add(other)
    }

    fun put(buffer: FloatBuffer) {
        buffer.put(x).put(y).put(z)
        buffer.flip()
    }

    fun toBuffer(): FloatBuffer {
        put(BUFFER)
        return BUFFER
    }

    fun dot(other: Vector3f) : Float = x * other.x + y * other.y + z * other.z

}