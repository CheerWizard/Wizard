package application.core.math

import java.nio.FloatBuffer

open class Vector4f {
    var x: Float
    var y: Float
    var z: Float
    var w: Float

    companion object {
        const val SIZE = 4
    }

    /**
     * Creates a default 4-tuple vector with all values set to 0.
     */
    constructor() {
        x = 0f
        y = 0f
        z = 0f
        w = 0f
    }

    /**
     * Creates a 4-tuple vector with specified values.
     *
     * @param x x value
     * @param y y value
     * @param z z value
     * @param w w value
     */
    constructor(x: Float, y: Float, z: Float, w: Float) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    fun byteSize(): Int = SIZE * Float.SIZE_BYTES

    /**
     * Calculates the squared length of the vector.
     *
     * @return Squared length of this vector
     */
    fun lengthSquared(): Float {
        return x * x + y * y + z * z + w * w
    }

    /**
     * Calculates the length of the vector.
     *
     * @return Length of this vector
     */
    fun length(): Float {
        return Math.sqrt(lengthSquared().toDouble()).toFloat()
    }

    /**
     * Normalizes the vector.
     *
     * @return Normalized vector
     */
    fun normalize(): Vector4f {
        val length = length()
        return divide(length)
    }

    /**
     * Adds this vector to another vector.
     *
     * @param other The other vector
     *
     * @return Sum of this + other
     */
    fun add(other: Vector4f): Vector4f {
        val x = x + other.x
        val y = y + other.y
        val z = z + other.z
        val w = w + other.w
        return Vector4f(x, y, z, w)
    }

    /**
     * Negates this vector.
     *
     * @return Negated vector
     */
    fun negate(): Vector4f {
        return scale(-1f)
    }

    /**
     * Subtracts this vector from another vector.
     *
     * @param other The other vector
     *
     * @return Difference of this - other
     */
    fun subtract(other: Vector4f): Vector4f {
        return add(other.negate())
    }

    /**
     * Multiplies a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     *
     * @return Scalar product of this * scalar
     */
    fun scale(scalar: Float): Vector4f {
        val x = x * scalar
        val y = y * scalar
        val z = z * scalar
        val w = w * scalar
        return Vector4f(x, y, z, w)
    }

    /**
     * Divides a vector by a scalar.
     *
     * @param scalar Scalar to multiply
     *
     * @return Scalar quotient of this / scalar
     */
    fun divide(scalar: Float): Vector4f {
        return scale(1f / scalar)
    }

    /**
     * Calculates the dot product of this vector with another vector.
     *
     * @param other The other vector
     *
     * @return Dot product of this * other
     */
    fun dot(other: Vector4f): Float {
        return x * other.x + y * other.y + z * other.z + w * other.w
    }

    /**
     * Calculates a linear interpolation between this vector with another
     * vector.
     *
     * @param other The other vector
     * @param alpha The alpha value, must be between 0.0 and 1.0
     *
     * @return Linear interpolated vector
     */
    fun lerp(other: Vector4f, alpha: Float): Vector4f {
        return scale(1f - alpha).add(other.scale(alpha))
    }

    /**
     * Stores the vector in a given Buffer.
     *
     * @param buffer The buffer to store the vector data
     */
    fun toBuffer(buffer: FloatBuffer) {
        buffer.put(x).put(y).put(z).put(w)
        buffer.flip()
    }

    fun mul(matrix4f: Matrix4f) : Vector4f {

        return this
    }

}