package application.platform.shader

import org.lwjgl.opengl.GL20.GL_VERTEX_SHADER
import org.lwjgl.opengl.GL20.glCreateShader

class GLVertexShader(fileName: String) : GLShader(fileName) {
    override var id: Int = glCreateShader(GL_VERTEX_SHADER)
}