package application.platform.hud

import application.graphics.hud.TextParser
import application.graphics.texture.Texture
import application.graphics.geometry.VertexBuffer
import application.platform.texture.GLTexture2d
import application.platform.geometry.GLVertexBuffer

class GLTextParser : TextParser() {

    override fun createTexture() : Texture {
        texture = GLTexture2d(strengthUniformName = "")
        return texture
    }

}