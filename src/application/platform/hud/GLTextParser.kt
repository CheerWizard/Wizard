package application.platform.hud

import application.graphics.tools.TextParser
import application.graphics.core.texture.Texture
import application.platform.texture.GLTexture2d

class GLTextParser : TextParser() {

    override fun createTexture() : Texture {
        texture = GLTexture2d(strengthUniformName = "")
        return texture
    }

}