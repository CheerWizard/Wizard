package application.core.math

import java.nio.FloatBuffer
import kotlin.math.sqrt

class Vector2f(
    var x : Float = 0f,
    var y : Float = 0f
) {

    companion object {
        const val SIZE = 2
    }

    fun byteSize(): Int = SIZE * Float.SIZE_BYTES

    fun squaredLength(): Float = x * x + y * y

    fun length(): Float = sqrt(squaredLength().toDouble()).toFloat()

    fun normalize(): Vector2f = divide(length())

    fun add(other: Vector2f): Vector2f {
        x += other.x
        y += other.y
        return this
    }

    fun negate(): Vector2f {
        return scale(-1f)
    }

    fun subtract(other: Vector2f): Vector2f = add(other.negate())

    fun scale(scalar: Float): Vector2f {
        x *= scalar
        y *= scalar
        return this
    }

    fun divide(scalar: Float): Vector2f = scale(1f / scalar)

    fun dot(other: Vector2f): Float = x * other.x + y * other.y

    fun lerp(other: Vector2f, alpha: Float): Vector2f = scale(1f - alpha).add(other.scale(alpha))

    fun toBuffer(buffer: FloatBuffer) {
        buffer.put(x).put(y)
        buffer.flip()
    }

}