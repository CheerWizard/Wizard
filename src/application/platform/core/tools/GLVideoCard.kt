package application.platform.core.tools

import application.core.tools.VideoCard
import org.lwjgl.opengl.GL30.*

class GLVideoCard : VideoCard() {
    override val renderer: String = glGetString(GL_RENDERER) as String
    override val vendor: String = glGetString(GL_VENDOR) as String
    override val version: String = glGetString(GL_VERSION) as String
    override val shaderLanguage: String = glGetString(GL_SHADING_LANGUAGE_VERSION) as String
    override val extensions: String = glGetString(GL_EXTENSIONS) as String
}