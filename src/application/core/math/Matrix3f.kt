package application.core.math

import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

class Matrix3f(
    var m00: Float = 1f,
    var m01: Float = 0f,
    var m02: Float = 0f,
    var m10: Float = 0f,
    var m11: Float = 1f,
    var m12: Float = 0f,
    var m20: Float = 0f,
    var m21: Float = 0f,
    var m22: Float = 1f
) {

    companion object {
        private val BUFFER = BufferUtils.createFloatBuffer(9)
    }

    constructor(col1: Vector3f, col2: Vector3f, col3: Vector3f) : this(
        m00 = col1.x,
        m10 = col1.y,
        m20 = col1.z,
        m01 = col2.x,
        m11 = col2.y,
        m21 = col2.z,
        m02 = col3.x,
        m12 = col3.y,
        m22 = col3.z,
    )

    fun add(other: Matrix3f) {
        m00 += other.m00
        m10 += other.m10
        m20 += other.m20
        m01 += other.m01
        m11 += other.m11
        m21 += other.m21
        m02 += other.m02
        m12 += other.m12
        m22 += other.m22
    }

    fun negate() = multiply(-1f)

    fun subtract(other: Matrix3f) {
        other.negate()
        add(other)
    }

    fun multiply(scalar: Float) {
        m00 *= scalar
        m10 *= scalar
        m20 *= scalar
        m01 *= scalar
        m11 *= scalar
        m21 *= scalar
        m02 *= scalar
        m12 *= scalar
        m22 *= scalar
    }

    fun multiply(vector: Vector3f): Vector3f {
        val x = m00 * vector.x + m01 * vector.y + m02 * vector.z
        val y = m10 * vector.x + m11 * vector.y + m12 * vector.z
        val z = m20 * vector.x + m21 * vector.y + m22 * vector.z
        return Vector3f(x, y, z)
    }

    fun multiply(other: Matrix3f) {
        m00 = m00 * other.m00 + m01 * other.m10 + m02 * other.m20
        m10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20
        m20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20
        m01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21
        m11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21
        m21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21
        m02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22
        m12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22
        m22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22
    }

    fun transpose() {
        m10 = m01
        m20 = m02
        m01 = m10
        m21 = m12
        m02 = m20
        m12 = m21
        m01 = 0f
        m02 = 0f
        m10 = 0f
        m12 = 0f
        m20 = 0f
        m21 = 0f
    }

    fun put(buffer: FloatBuffer) {
        buffer.put(m00).put(m10).put(m20)
        buffer.put(m01).put(m11).put(m21)
        buffer.put(m02).put(m12).put(m22)
        buffer.flip()
    }

    fun toBuffer(): FloatBuffer {
        put(BUFFER)
        return BUFFER
    }

}