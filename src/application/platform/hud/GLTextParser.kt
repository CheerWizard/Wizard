package application.platform.hud

import application.graphics.hud.TextParser
import application.graphics.texture.Texture
import application.graphics.geometry.VertexBuffer
import application.platform.texture.GLTexture2d
import application.platform.vertex.GLVertexBuffer

class GLTextParser : TextParser() {

    override fun createTextureBuffer(vertexName: String): VertexBuffer = GLVertexBuffer()

    override fun createVertexBuffer(vertexName: String): VertexBuffer = GLVertexBuffer()

    override fun createTexture() : Texture {
        texture = GLTexture2d(samplerUniformName = "", strengthUniformName = "")
        return texture
    }

}