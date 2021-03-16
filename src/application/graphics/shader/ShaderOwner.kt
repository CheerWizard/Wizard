package application.graphics.shader

import application.core.Destroyable
import application.graphics.core.buffers.MaterialBuffer
import application.graphics.core.buffers.MeshBuffer
import application.graphics.core.buffers.VertexArray
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f

abstract class ShaderOwner(
    protected val vertexShader: Shader,
    protected val fragmentShader: Shader
) : Destroyable {

    abstract var id: Int

    protected val uniformLocations = HashMap<String, Int>()

    private var isCreated = false
    private var isPrepared = false
    private var isDestroyed = false

    abstract val vertexArray: VertexArray
    abstract val meshBuffer: MeshBuffer
    abstract val materialBuffer: MaterialBuffer

    open fun onCreate() {
        if (isCreated) return
        isCreated = true

        vertexShader.onCreate()
        fragmentShader.onCreate()
    }

    open fun onPrepare() {
        findBuffersLocation()
        prepareBuffers()

        if (isPrepared) return
        isPrepared = true
    }

    private fun findBuffersLocation() {
        val meshAttributes = meshBuffer.getAttributes()
        val materialAttributes = materialBuffer.getAttributes()

        for (attribute in meshAttributes) {
            findAttributeLocation(attribute)
        }

        for (attribute in materialAttributes) {
            findAttributeLocation(attribute)
        }
    }

    private fun prepareBuffers() {
        vertexArray.bind()
        meshBuffer.prepare()
        materialBuffer.prepare()
        vertexArray.unbind()
    }

    open fun onStart() {
        vertexArray.bind()
        meshBuffer.enableAttributes()
        materialBuffer.enableAttributes()
    }

    open fun onStop() {
        materialBuffer.disableAttributes()
        meshBuffer.disableAttributes()
        vertexArray.unbind()
    }

    override fun onDestroy() {
        if (isDestroyed) return
        isDestroyed = true

        fragmentShader.onDestroy()
        vertexShader.onDestroy()
        uniformLocations.clear()

        meshBuffer.onDestroy()
        materialBuffer.onDestroy()
    }

    fun createPositionBuffer(name: String) {
        meshBuffer.createPositionBuffer(name)
    }

    fun createNormalBuffer(name: String) {
        meshBuffer.createNormalBuffer(name)
    }

    fun createCoordinatesBuffer(name: String) {
        materialBuffer.createCoordinatesBuffer(name)
    }

    fun createColorsBuffer(name: String) {
        materialBuffer.createColorsBuffer(name)
    }

    abstract fun putUniformName(uniformName: String)
    abstract fun setUniform(uniformName: String, uniformValue: Matrix4f)
    abstract fun setUniform(uniformName: String, uniformValue: Vector4f)
    abstract fun setUniform(uniformName: String, uniformValue: Vector3f)
    abstract fun setUniform(uniformName: String, uniformValue: Vector2f)
    abstract fun setUniform(uniformName: String, uniformValue: Float)
    abstract fun setUniform(uniformName: String, uniformValue: Int)
    abstract fun setUniform(uniformName: String, uniformValue: IntArray)
    abstract fun setUniformTrue(uniformName: String)
    abstract fun setUniformFalse(uniformName: String)

    protected abstract fun getFragmentShader(fileName: String): Shader
    protected abstract fun getVertexShader(fileName: String): Shader
    protected abstract fun findAttributeLocation(attribute: Attribute)

}