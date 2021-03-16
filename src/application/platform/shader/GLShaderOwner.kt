package application.platform.shader

import application.graphics.core.buffers.MaterialBuffer
import application.graphics.core.buffers.MeshBuffer
import application.graphics.core.buffers.VertexArray
import application.graphics.shader.Attribute
import application.graphics.shader.Shader
import application.graphics.shader.ShaderOwner
import application.platform.core.buffers.GLMaterialBuffer
import application.platform.core.buffers.GLMeshBuffer
import application.platform.core.buffers.GLVertexArray
import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*
import java.nio.FloatBuffer

class GLShaderOwner(vertexShader: Shader, fragmentShader: Shader) : ShaderOwner(vertexShader = vertexShader, fragmentShader = fragmentShader) {

    companion object {
        private val MAT4F_BUFFER = BufferUtils.createFloatBuffer(16)

        fun toBuffer(matrix4f: Matrix4f) : FloatBuffer {
            return MAT4F_BUFFER.apply {
                clear()
                put(matrix4f.m00())
                put(matrix4f.m01())
                put(matrix4f.m02())
                put(matrix4f.m03())
                put(matrix4f.m10())
                put(matrix4f.m11())
                put(matrix4f.m12())
                put(matrix4f.m13())
                put(matrix4f.m20())
                put(matrix4f.m21())
                put(matrix4f.m22())
                put(matrix4f.m23())
                put(matrix4f.m30())
                put(matrix4f.m31())
                put(matrix4f.m32())
                put(matrix4f.m33())
                flip()
            }
        }
    }

    override var id: Int = 0

    override val vertexArray: VertexArray = GLVertexArray()
    override val meshBuffer: MeshBuffer = GLMeshBuffer()
    override val materialBuffer: MaterialBuffer = GLMaterialBuffer()

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

    override fun onStart() {
        glUseProgram(id)
        super.onStart()
    }
    override fun onStop() {
        super.onStop()
        glUseProgram(0)
    }

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

    override fun findAttributeLocation(attribute: Attribute) {
        val attributeLocation = glGetAttribLocation(id, attribute.name)
        glBindAttribLocation(id, attributeLocation, attribute.name)
        attribute.location = attributeLocation
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
            glUniformMatrix4fv(l, false, toBuffer(uniformValue))
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