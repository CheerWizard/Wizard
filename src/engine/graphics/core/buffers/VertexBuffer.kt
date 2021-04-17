package engine.graphics.core.buffers

import engine.core.UBuffer
import engine.core.tools.Environment
import engine.graphics.shader.attributes.Attribute
import engine.graphics.shader.attributes.AttributeList
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

abstract class VertexBuffer(
    var totalVertexCount: Int = Environment.VERTEX_COUNT,
    val attributes: AttributeList = AttributeList()
) : UBuffer<FloatBuffer>() {

    abstract var id: Int

    abstract var polygonMode: Int

    override var buffer: FloatBuffer = BufferUtils.createFloatBuffer(totalVertexCount * attributes.getTotalSize())
    override var subBuffer: FloatBuffer = BufferUtils.createFloatBuffer(attributes.getTotalSize())

    private var currentVertexCount = 0

    fun getVertexCount(): Int = currentVertexCount

    abstract fun enableAttributes()
    abstract fun disableAttributes()

    fun addAttribute(attribute: Attribute) {
        attributes.add(attribute)
    }

    fun clearAttributes() {
        attributes.clear()
    }

    override fun allocateBuffer() {
        buffer = BufferUtils.createFloatBuffer(totalVertexCount * attributes.getTotalSize())
    }

    fun allocateBuffer(totalInstanceCount: Int) {
        buffer = BufferUtils.createFloatBuffer(totalInstanceCount * attributes.getTotalSize())
    }

    override fun allocateSubBuffer() {
        subBuffer = BufferUtils.createFloatBuffer(attributes.getTotalSize())
    }

    fun allocateAttribute(attributeStart: Int, attributeSize: Int) {
        subBufferOffset = (attributeStart * Int.SIZE_BYTES).toLong()
        subBuffer = BufferUtils.createFloatBuffer(attributeSize)
    }

    fun allocateVertex(vertexStart: Int) {
        subBufferOffset = (vertexStart * Int.SIZE_BYTES).toLong()
        allocateSubBuffer()
    }

    fun allocateMesh(vertexStart: Int, vertexCount: Int) {
        subBufferOffset = (vertexStart * Int.SIZE_BYTES).toLong()
        subBuffer = BufferUtils.createFloatBuffer(vertexCount * attributes.getTotalSize())
    }

    fun addVertexCount(vertexCount: Int) {
        currentVertexCount += vertexCount
    }

    fun add(float: Float) {
        buffer.put(float)
    }

    fun add(data: FloatArray) {
        buffer.put(data)
    }

    fun add(vector3f: Vector3f) {
        buffer.put(vector3f.x)
        buffer.put(vector3f.y)
        buffer.put(vector3f.z)
    }

    fun add(vector4f: Vector4f) {
        buffer.put(vector4f.x)
        buffer.put(vector4f.y)
        buffer.put(vector4f.z)
        buffer.put(vector4f.w)
    }

    fun add(vector2f: Vector2f) {
        buffer.put(vector2f.x)
        buffer.put(vector2f.y)
    }

    fun add(x: Float, y: Float, z: Float) {
        buffer.put(x)
        buffer.put(y)
        buffer.put(z)
    }

    fun add(matrix4f: Matrix4f) {
        buffer.put(matrix4f.m00())
        buffer.put(matrix4f.m01())
        buffer.put(matrix4f.m02())
        buffer.put(matrix4f.m03())

        buffer.put(matrix4f.m10())
        buffer.put(matrix4f.m11())
        buffer.put(matrix4f.m12())
        buffer.put(matrix4f.m13())

        buffer.put(matrix4f.m20())
        buffer.put(matrix4f.m21())
        buffer.put(matrix4f.m22())
        buffer.put(matrix4f.m23())

        buffer.put(matrix4f.m30())
        buffer.put(matrix4f.m31())
        buffer.put(matrix4f.m32())
        buffer.put(matrix4f.m33())
    }

    fun update(float: Float) {
        subBuffer.put(float)
    }

    fun update(data: FloatArray) {
        subBuffer.put(data)
    }

    fun update(vector3f: Vector3f) {
        subBuffer.put(vector3f.x)
        subBuffer.put(vector3f.y)
        subBuffer.put(vector3f.z)
    }

    fun update(vector4f: Vector4f) {
        subBuffer.put(vector4f.x)
        subBuffer.put(vector4f.y)
        subBuffer.put(vector4f.z)
        subBuffer.put(vector4f.w)
    }

    fun update(vector2f: Vector2f) {
        subBuffer.put(vector2f.x)
        subBuffer.put(vector2f.y)
    }

    fun update(x: Float, y: Float, z: Float) {
        subBuffer.put(x)
        subBuffer.put(y)
        subBuffer.put(z)
    }

    fun update(matrix4f: Matrix4f) {
        subBuffer.put(matrix4f.m00())
        subBuffer.put(matrix4f.m01())
        subBuffer.put(matrix4f.m02())
        subBuffer.put(matrix4f.m03())

        subBuffer.put(matrix4f.m10())
        subBuffer.put(matrix4f.m11())
        subBuffer.put(matrix4f.m12())
        subBuffer.put(matrix4f.m13())

        subBuffer.put(matrix4f.m20())
        subBuffer.put(matrix4f.m21())
        subBuffer.put(matrix4f.m22())
        subBuffer.put(matrix4f.m23())

        subBuffer.put(matrix4f.m30())
        subBuffer.put(matrix4f.m31())
        subBuffer.put(matrix4f.m32())
        subBuffer.put(matrix4f.m33())
    }

    override fun reset() {
        super.reset()
        currentVertexCount = 0
        clearAttributes()
    }

}