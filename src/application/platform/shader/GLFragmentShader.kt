package application.platform.shader

import org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER
import org.lwjgl.opengl.GL20.glCreateShader

class GLFragmentShader(fileName: String) : GLShader(fileName) {
    override var id: Int = glCreateShader(GL_FRAGMENT_SHADER)
}