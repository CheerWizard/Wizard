package application.core.math

import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

class Matrix2f(
    var m00: Float = 1f,
    var m01: Float = 0f,
    var m10: Float = 0f,
    var m11: Float = 1f
) {

    companion object {
        private val BUFFER = BufferUtils.createFloatBuffer(4)
    }

    constructor(col1: Vector2f, col2: Vector2f) : this(
        m00 = col1.x,
        m10 = col1.y,
        m01 = col2.x,
        m11 = col2.y
    )

    fun add(other: Matrix2f) {
        m00 += other.m00
        m10 += other.m10
        m01 += other.m01
        m11 += other.m11
    }

    fun negate() = multiply(-1f)

    fun subtract(other: Matrix2f) {
       other.negate()
       add(other)
    }

    fun multiply(scalar: Float) {
        m00 *= scalar
        m10 *= scalar
        m01 *= scalar
        m11 *= scalar
    }

    fun multiply(vector: Vector2f): Vector2f {
        val x = m00 * vector.x + m01 * vector.y
        val y = m10 * vector.x + m11 * vector.y
        return Vector2f(x, y)
    }

    fun multiply(other: Matrix2f) {
        m00 = m00 * other.m00 + m01 * other.m10
        m10 = m10 * other.m00 + m11 * other.m10
        m01 = m00 * other.m01 + m01 * other.m11
        m11 = m10 * other.m01 + m11 * other.m11
    }

    fun transpose() {
        m10 = m01
        m01 = m10
        m01 = 0f
        m10 = 0f
    }

    fun put(buffer: FloatBuffer) {
        buffer.put(m00).put(m10)
        buffer.put(m01).put(m11)
        buffer.flip()
    }

    fun toBuffer(): FloatBuffer {
        put(BUFFER)
        return BUFFER
    }

}