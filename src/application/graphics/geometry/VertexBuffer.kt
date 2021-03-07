package application.graphics.geometry

import application.core.Destroyable
import application.core.math.Vector2f
import application.core.math.Vector3f
import org.joml.Matrix4f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

abstract class VertexBuffer(
        totalVertexCount: Int = MAX_VERTEX_COUNT,
        val attribute: Attribute,
        private var cachedData: FloatBuffer? = null
) : Destroyable {

    companion object {
        const val MAX_VERTEX_COUNT = 100000
    }

    abstract var id: Int

    abstract var polygonMode: Int

    val data: FloatBuffer = BufferUtils.createFloatBuffer(totalVertexCount * attribute.size())

    fun capacity(): Int = data.capacity()

    fun position(): Int = data.position()

    fun getVertexCount(): Int = currentVertexCount

    fun setPosition(newPosition: Int) = data.position(newPosition)

    abstract fun enableAttributes()
    abstract fun disableAttributes()

    private var currentVertexCount = 0

    fun createCachedData() {
        cachedData = BufferUtils.createFloatBuffer(capacity())
    }

    fun add(data: FloatArray) : Int {
        this.data.put(data)
        cachedData?.put(data)

        return currentVertexCount++
    }

    fun add(vector3f: Vector3f) : Int {
        data.put(vector3f.x)
        data.put(vector3f.y)
        data.put(vector3f.z)

        cachedData?.let { cd ->
            cd.put(vector3f.x)
            cd.put(vector3f.y)
            cd.put(vector3f.z)
        }

        return currentVertexCount++
    }

    fun add(vector4f: Vector4f): Int {
        data.put(vector4f.x)
        data.put(vector4f.y)
        data.put(vector4f.z)
        data.put(vector4f.w)

        cachedData?.let { cd ->
            cd.put(vector4f.x)
            cd.put(vector4f.y)
            cd.put(vector4f.z)
            cd.put(vector4f.w)
        }

        return currentVertexCount++
    }

    fun add(vector2f: Vector2f): Int {
        data.put(vector2f.x)
        data.put(vector2f.y)

        cachedData?.let { cd ->
            cd.put(vector2f.x)
            cd.put(vector2f.y)
        }

        return currentVertexCount++
    }

    fun update(vertexId: Int, vector2f: Vector2f) {
        var bufferIndex = vertexId * attribute.size()

        data.run {
            put(bufferIndex++, vector2f.x)
            put(bufferIndex, vector2f.y)
        }
    }

    fun getVector2f(vertexId: Int) : Vector2f {
        var bufferIndex = vertexId * attribute.size()

        return Vector2f(
                x = data.get(bufferIndex++),
                y = data.get(bufferIndex)
        )
    }

    fun clear() {
        data.clear()
        cachedData?.clear()
        currentVertexCount = 0
    }

    fun getVector3f(vertexId: Int): Vector3f {
        var bufferIndex = vertexId * attribute.size()

        return Vector3f(
                x = data.get(bufferIndex++),
                y = data.get(bufferIndex++),
                z = data.get(bufferIndex)
        )
    }

    private val transformVector4f = Vector4f()

    fun transformVertex(vertexId: Int, matrix4f: Matrix4f) {
        cachedData?.let { cd ->
            val bufferIndex = vertexId * attribute.size()

            val x = cd.get(bufferIndex)
            val y = cd.get(bufferIndex + 1)
            val z = cd.get(bufferIndex + 2)

            transformVector4f.x = x
            transformVector4f.y = y
            transformVector4f.z = z

            transformVector4f.mul(matrix4f)

            data.put(bufferIndex, transformVector4f.x)
            data.put(bufferIndex + 1, transformVector4f.y)
            data.put(bufferIndex + 2, transformVector4f.z)

            transformVector4f.w = 1f
        }
    }

    fun flip() {
        data.flip()
    }

    abstract fun prepare()
    abstract fun bind()
    abstract fun unbind()

    override fun onDestroy() {
        clear()
    }

}