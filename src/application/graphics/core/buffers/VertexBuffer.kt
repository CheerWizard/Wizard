package application.graphics.core.buffers

import application.core.Destroyable
import application.core.tools.Environment
import application.graphics.shader.Attribute
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

abstract class VertexBuffer(
    totalVertexCount: Int = Environment.VERTEX_COUNT,
    val attribute: Attribute
) : Destroyable {

    abstract var id: Int

    abstract var polygonMode: Int

    private var isWritable = true

    val data: FloatBuffer = BufferUtils.createFloatBuffer(totalVertexCount * attribute.size())

    fun capacity(): Int = data.capacity()

    fun position(): Int = data.position()

    fun getVertexCount(): Int = currentVertexCount

    abstract fun enableAttributes()
    abstract fun disableAttributes()

    private var currentVertexCount = 0

    fun fill(vector4f: Vector4f, vertexStart: Int = 0, vertexEnd: Int) {
        var vertexId = vertexStart
        while (vertexId < vertexEnd) {
            if (vertexId * attribute.size() < position()) {
                update(vertexId = vertexId, vector4f = vector4f)
            } else {
                add(vector4f)
            }
            vertexId++
        }
    }

    fun add(data: FloatArray) {
        this.data.put(data)
        currentVertexCount += data.size / attribute.size()
    }

    fun add(vector3f: Vector3f) : Int {
        data.put(vector3f.x)
        data.put(vector3f.y)
        data.put(vector3f.z)

        return currentVertexCount++
    }

    fun add(vector4f: Vector4f): Int {
        data.put(vector4f.x)
        data.put(vector4f.y)
        data.put(vector4f.z)
        data.put(vector4f.w)

        return currentVertexCount++
    }

    fun add(vector2f: Vector2f): Int {
        data.put(vector2f.x)
        data.put(vector2f.y)

        return currentVertexCount++
    }

    fun update(vertexId: Int, vector4f: Vector4f) {
        var bufferIndex = vertexId * attribute.size()

        data.run {
            put(bufferIndex++, vector4f.x)
            put(bufferIndex++, vector4f.y)
            put(bufferIndex++, vector4f.z)
            put(bufferIndex, vector4f.w)
        }
    }

    fun clear() {
        writeMode()
        currentVertexCount = 0
    }

    fun writeMode() {
        if (!isWritable) {
            isWritable = true
            data.clear()
        }
    }

    fun readMode() {
        if (isWritable) {
            isWritable = false
            data.flip()
        }
    }

    abstract fun prepare()
    abstract fun bind()
    abstract fun unbind()

    override fun onDestroy() {
        clear()
    }

}