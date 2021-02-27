package application.platform.shader

import application.graphics.shader.Shader
import org.lwjgl.opengl.GL20.*

abstract class GLShader(fileName: String) : Shader(fileName) {

    override val storagePath: String = "res/shaders/glsl"

    override fun onCreate() {
        glShaderSource(id, source)
        glCompileShader(id)

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error occurs during shader compilation!")
            System.err.println(glGetShaderInfoLog(id, 500))
        }
    }

    override fun onDestroy() {
        glDeleteShader(id)
    }

}