package application.platform.shader

import application.core.math.Matrix4f
import application.core.math.Vector2f
import application.core.math.Vector3f
import application.core.math.Vector4f
import application.graphics.shader.Shader
import application.graphics.shader.ShaderOwner
import org.lwjgl.opengl.GL20.*

class GLShaderOwner(vertexShader: Shader, fragmentShader: Shader) : ShaderOwner(vertexShader = vertexShader, fragmentShader = fragmentShader) {

    override var id: Int = 0

    override fun getFragmentShader(fileName: String): Shader = GLFragmentShader(fileName = fileName)
    override fun getVertexShader(fileName: String): Shader = GLVertexShader(fileName = fileName)

    override fun onCreate() {
        super.onCreate()
        id = glCreateProgram()
        attachShaders()
    }

    override fun onPrepare() {
        super.onPrepare()
        glLinkProgram(id)
        glValidateProgram(id)
    }

    private fun attachShaders() {
        glAttachShader(id, vertexShader.id)
        glAttachShader(id, fragmentShader.id)
    }

    override fun onStart() = glUseProgram(id)
    override fun onStop() = glUseProgram(0)

    override fun onDestroy() {
        super.onDestroy()
        detachShaders()
        glDeleteProgram(id)
    }

    private fun detachShaders() {
        glDetachShader(id, vertexShader.id)
        glDetachShader(id, fragmentShader.id)
    }

    override fun putUniformName(uniformName: String) {
        uniformLocations[uniformName] = glGetUniformLocation(id, uniformName)
    }

    override fun setAttribute(attributeName: String) {
        val attributeLocation = glGetAttribLocation(id, attributeName)
        setAttribute(attributeName = attributeName, attributeLocation = attributeLocation)
    }

    override fun setAttribute(attributeName: String, attributeLocation: Int) {
        glBindAttribLocation(id, attributeLocation, attributeName)
    }

    override fun setUniform(uniformName: String, uniformValue: Float) {
        uniformLocations[uniformName]?.let { l->
            glUniform1f(l, uniformValue)
        }
    }

    override fun setUniform(uniformName: String, uniformValue: Int) {
        uniformLocations[uniformName]?.let { l->
            glUniform1i(l, uniformValue)
        }
    }

    override fun setUniform(uniformName: String, uniformValue: IntArray) {
        uniformLocations[uniformName]?.let { l->
            glUniform1iv(l, uniformValue)
        }
    }

    override fun setUniform(uniformName: String, uniformValue: Matrix4f) {
        uniformLocations[uniformName]?.let { l->
            glUniformMatrix4fv(l, false, uniformValue.toBuffer())
        }
    }

    override fun setUniform(uniformName: String, uniformValue: Vector4f) {
        uniformLocations[uniformName]?.let { l->
            glUniform4f(l, uniformValue.x, uniformValue.y, uniformValue.z, uniformValue.w)
        }
    }

    override fun setUniform(uniformName: String, uniformValue: Vector3f) {
        uniformLocations[uniformName]?.let { l->
            glUniform3f(l, uniformValue.x, uniformValue.y, uniformValue.z)
        }
    }

    override fun setUniform(uniformName: String, uniformValue: Vector2f) {
        uniformLocations[uniformName]?.let { l->
            glUniform2f(l, uniformValue.x, uniformValue.y)
        }
    }

    override fun setUniformFalse(uniformName: String) {
        uniformLocations[uniformName]?.let { l->
            glUniform1f(l, 0f)
        }
    }

    override fun setUniformTrue(uniformName: String) {
        uniformLocations[uniformName]?.let { l->
            glUniform1f(l, 1f)
        }
    }

}