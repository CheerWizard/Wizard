package engine.platform.shader

import engine.graphics.core.buffers.MeshBuffer
import engine.graphics.core.buffers.VertexArray
import engine.graphics.shader.Shader
import engine.graphics.shader.ShaderOwner
import engine.graphics.shader.attributes.Attribute
import engine.graphics.shader.uniforms.*
import engine.platform.core.buffers.GLIndexBuffer
import engine.platform.core.buffers.GLVertexArray
import engine.platform.core.buffers.GLVertexBuffer
import engine.platform.texture.GLTextureBuffer2D
import engine.platform.texture.GLTextureBufferCubeMap
import org.joml.Matrix4f
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

    override val meshBuffer: MeshBuffer = MeshBuffer(
        indexBuffer = GLIndexBuffer(),
        vertexBuffer = GLVertexBuffer(),
        instanceBuffer = GLVertexBuffer()
    )

    override fun getFragmentShader(fileName: String): Shader = GLFragmentShader(fileName = fileName)
    override fun getVertexShader(fileName: String): Shader = GLVertexShader(fileName = fileName)

    override fun createTexture2D(fileName: String, slot: Int) {
        val textureBuffer = GLTextureBuffer2D(slot = slot).create(fileName)
        textureBuffers.add(textureBuffer)
    }

    override fun createTextureCubeMap(fileName: String, slot: Int) {
        val textureBuffer = GLTextureBufferCubeMap(slot = slot).create(fileName)
        textureBuffers.add(textureBuffer)
    }

    override fun onCreate() {
        super.onCreate()
        id = glCreateProgram()
        attachShaders()
    }

    override fun onPrepare() {
        glLinkProgram(id)
        glValidateProgram(id)
        super.onPrepare()
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

    override fun onUpdateUniform(uniform: UMatrix4f) {
        uniformLocations[uniform.name]?.let { l->
            glUniformMatrix4fv(l, false, toBuffer(uniform))
        }
    }

    override fun onUpdateUniform(uniform: UFloat) {
        uniformLocations[uniform.name]?.let { l->
            glUniform1f(l, uniform.value)
        }
    }

    override fun onUpdateUniform(uniform: UInteger) {
        uniformLocations[uniform.name]?.let { l->
            glUniform1i(l, uniform.value)
        }
    }

    override fun onUpdateUniform(uniform: UIntegerArray) {
        uniformLocations[uniform.name]?.let { l->
            glUniform1iv(l, uniform.data)
        }
    }

    override fun onUpdateUniform(uniform: UVector2f) {
        uniformLocations[uniform.name]?.let { l->
            glUniform2f(l, uniform.data.x, uniform.data.y)
        }
    }

    override fun onUpdateUniform(uniform: UVector3f) {
        uniformLocations[uniform.name]?.let { l->
            glUniform3f(l, uniform.data.x, uniform.data.y, uniform.data.z)
        }
    }

    override fun onUpdateUniform(uniform: UVector4f) {
        uniformLocations[uniform.name]?.let { l->
            glUniform4f(l, uniform.data.x, uniform.data.y, uniform.data.z, uniform.data.w)
        }
    }

    override fun updateUniformFalse(uniformName: String) {
        uniformLocations[uniformName]?.let { l->
            glUniform1f(l, 0f)
        }
    }

    override fun updateUniformTrue(uniformName: String) {
        uniformLocations[uniformName]?.let { l->
            glUniform1f(l, 1f)
        }
    }

    override fun onUpdateUniform(name: String, value: Int) {
        uniformLocations[name]?.let { l->
            glUniform1i(l, value)
        }
    }

}