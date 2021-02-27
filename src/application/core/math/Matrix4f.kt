package application.core.math

import org.lwjgl.BufferUtils
import java.nio.FloatBuffer
import kotlin.math.tan

open class Matrix4f(
    var m00: Float = 1f,
    var m01: Float = 0f,
    var m02: Float = 0f,
    var m03: Float = 0f,
    var m10: Float = 0f,
    var m11: Float = 1f,
    var m12: Float = 0f,
    var m13: Float = 0f,
    var m20: Float = 0f,
    var m21: Float = 0f,
    var m22: Float = 1f,
    var m23: Float = 0f,
    var m30: Float = 0f,
    var m31: Float = 0f,
    var m32: Float = 0f,
    var m33: Float = 1f
) : org.joml.Matrix4f() {

    constructor(col1: Vector4f, col2: Vector4f, col3: Vector4f, col4: Vector4f) : this(
        m00 = col1.x,
        m10 = col1.y,
        m20 = col1.z,
        m30 = col1.w,
        m01 = col2.x,
        m11 = col2.y,
        m21 = col2.z,
        m31 = col2.w,
        m02 = col3.x,
        m12 = col3.y,
        m22 = col3.z,
        m32 = col3.w,
        m03 = col4.x,
        m13 = col4.y,
        m23 = col4.z,
        m33 = col4.w
    )

    fun add(other: Matrix4f) {
        m00 += other.m00
        m10 += other.m10
        m20 += other.m20
        m30 += other.m30
        m01 += other.m01
        m11 += other.m11
        m21 += other.m21
        m31 += other.m31
        m02 += other.m02
        m12 += other.m12
        m22 += other.m22
        m32 += other.m32
        m03 += other.m03
        m13 += other.m13
        m23 += other.m23
        m33 += other.m33
    }

    fun negate() = multiply(-1f)

    fun subtract(other: Matrix4f) {
        other.negate()
        add(other)
    }

    fun multiply(scalar: Float) {
        m00 *= scalar
        m10 *= scalar
        m20 *= scalar
        m30 *= scalar
        m01 *= scalar
        m11 *= scalar
        m21 *= scalar
        m31 *= scalar
        m02 *= scalar
        m12 *= scalar
        m22 *= scalar
        m32 *= scalar
        m03 *= scalar
        m13 *= scalar
        m23 *= scalar
        m33 *= scalar
    }

    fun multiply(vector: Vector4f): Vector4f {
        val x = m00 * vector.x + m01 * vector.y + m02 * vector.z + m03 * vector.w
        val y = m10 * vector.x + m11 * vector.y + m12 * vector.z + m13 * vector.w
        val z = m20 * vector.x + m21 * vector.y + m22 * vector.z + m23 * vector.w
        val w = m30 * vector.x + m31 * vector.y + m32 * vector.z + m33 * vector.w
        return Vector4f(x, y, z, w)
    }

    fun multiply(other: Matrix4f) {
        m00 = m00 * other.m00 + m01 * other.m10 + m02 * other.m20 + m03 * other.m30
        m10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20 + m13 * other.m30
        m20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20 + m23 * other.m30
        m30 = m30 * other.m00 + m31 * other.m10 + m32 * other.m20 + m33 * other.m30
        m01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21 + m03 * other.m31
        m11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21 + m13 * other.m31
        m21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21 + m23 * other.m31
        m31 = m30 * other.m01 + m31 * other.m11 + m32 * other.m21 + m33 * other.m31
        m02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22 + m03 * other.m32
        m12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22 + m13 * other.m32
        m22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22 + m23 * other.m32
        m32 = m30 * other.m02 + m31 * other.m12 + m32 * other.m22 + m33 * other.m32
        m03 = m00 * other.m03 + m01 * other.m13 + m02 * other.m23 + m03 * other.m33
        m13 = m10 * other.m03 + m11 * other.m13 + m12 * other.m23 + m13 * other.m33
        m23 = m20 * other.m03 + m21 * other.m13 + m22 * other.m23 + m23 * other.m33
        m33 = m30 * other.m03 + m31 * other.m13 + m32 * other.m23 + m33 * other.m33
    }

    fun toBuffer(): FloatBuffer {
        get(BUFFER)
        return BUFFER
    }

    fun translateX(x: Float) {
        m03 += x
    }

    fun translateY(y: Float) {
        m13 += y
    }

    fun translateZ(z: Float) {
        m23 += z
    }

    fun translate(vector3f: Vector3f) {
        translateX(vector3f.x)
        translateY(vector3f.y)
        translateZ(vector3f.z)
    }

    fun scaleX(x: Float) {
        m00 *= x
    }

    fun scaleY(y: Float) {
        m11 *= y
    }

    fun scaleZ(z: Float) {
        m22 *= z
    }

    companion object {

        private val BUFFER = BufferUtils.createFloatBuffer(16)

        /**
         * Creates a orthographic projection matrix. Similar to
         * `glOrtho(left, right, bottom, top, near, far)`.
         *
         * @param left   Coordinate for the left vertical clipping pane
         * @param right  Coordinate for the right vertical clipping pane
         * @param bottom Coordinate for the bottom horizontal clipping pane
         * @param top    Coordinate for the bottom horizontal clipping pane
         * @param near   Coordinate for the near depth clipping pane
         * @param far    Coordinate for the far depth clipping pane
         *
         * @return Orthographic matrix
         */
        fun orthographic(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f {
            val ortho = Matrix4f()
            val tx = -(right + left) / (right - left)
            val ty = -(top + bottom) / (top - bottom)
            val tz = -(far + near) / (far - near)
            ortho.m00 = 2f / (right - left)
            ortho.m11 = 2f / (top - bottom)
            ortho.m22 = -2f / (far - near)
            ortho.m03 = tx
            ortho.m13 = ty
            ortho.m23 = tz
            return ortho
        }

        /**
         * Creates a perspective projection matrix. Similar to
         * `glFrustum(left, right, bottom, top, near, far)`.
         *
         * @param left   Coordinate for the left vertical clipping pane
         * @param right  Coordinate for the right vertical clipping pane
         * @param bottom Coordinate for the bottom horizontal clipping pane
         * @param top    Coordinate for the bottom horizontal clipping pane
         * @param near   Coordinate for the near depth clipping pane, must be
         * positive
         * @param far    Coordinate for the far depth clipping pane, must be
         * positive
         *
         * @return Perspective matrix
         */
        fun frustum(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f {
            val frustum = Matrix4f()
            val a = (right + left) / (right - left)
            val b = (top + bottom) / (top - bottom)
            val c = -(far + near) / (far - near)
            val d = -(2f * far * near) / (far - near)
            frustum.m00 = 2f * near / (right - left)
            frustum.m11 = 2f * near / (top - bottom)
            frustum.m02 = a
            frustum.m12 = b
            frustum.m22 = c
            frustum.m32 = -1f
            frustum.m23 = d
            frustum.m33 = 0f
            return frustum
        }

        /**
         * Creates a perspective projection matrix. Similar to
         * `gluPerspective(fovy, aspec, zNear, zFar)`.
         *
         * @param fov   Field of view angle in degrees
         * @param aspect The aspect ratio is the ratio of width to height
         * @param near   Distance from the viewer to the near clipping plane, must
         * be positive
         * @param far    Distance from the viewer to the far clipping plane, must be
         * positive
         *
         * @return Perspective matrix
         */
        fun perspective(fov: Float, aspect: Float, near: Float, far: Float): Matrix4f {
            val perspective = Matrix4f()
            val f = (1f / tan(Math.toRadians(fov.toDouble()) / 2f)).toFloat()
            perspective.m00 = f / aspect
            perspective.m11 = f
            perspective.m22 = (far + near) / (near - far)
            perspective.m32 = -1f
            perspective.m23 = 2f * far * near / (near - far)
            perspective.m33 = 0f
            return perspective
        }


    }
}