package application.graphics.shader

import application.core.Destroyable
import application.core.math.Matrix4f
import application.core.math.Vector2f
import application.core.math.Vector3f
import application.core.math.Vector4f

abstract class ShaderOwner(
    protected val vertexShader: Shader,
    protected val fragmentShader: Shader
) : Destroyable {

    abstract var id: Int

    protected val uniformLocations = HashMap<String, Int>()

    private var isCreated = false
    private var isPrepared = false
    private var isDestroyed = false

    open fun onCreate() {
        if (isCreated) return
        isCreated = true

        vertexShader.onCreate()
        fragmentShader.onCreate()
    }

    open fun onPrepare() {
        if (isPrepared) return
        isPrepared = true
    }

    abstract fun onStart()
    abstract fun onStop()

    override fun onDestroy() {
        if (isDestroyed) return
        isDestroyed = true

        fragmentShader.onDestroy()
        vertexShader.onDestroy()
        uniformLocations.clear()
    }

    abstract fun putUniformName(uniformName: String)
    abstract fun setVertex(vertexAttribute: Int, vertexName: String)
    abstract fun setUniform(uniformName: String, uniformValue: Matrix4f)
    abstract fun setUniform(uniformName: String, uniformValue: Vector4f)
    abstract fun setUniform(uniformName: String, uniformValue: Vector3f)
    abstract fun setUniform(uniformName: String, uniformValue: Vector2f)
    abstract fun setUniform(uniformName: String, uniformValue: Float)
    abstract fun setUniform(uniformName: String, uniformValue: Int)
    abstract fun setUniformTrue(uniformName: String)
    abstract fun setUniformFalse(uniformName: String)

    protected abstract fun getFragmentShader(fileName: String): Shader
    protected abstract fun getVertexShader(fileName: String): Shader

}