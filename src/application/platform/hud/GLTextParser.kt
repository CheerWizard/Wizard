package application.platform.hud

import application.graphics.mesh.VertexBuffer
import application.graphics.hud.TextParser
import application.graphics.texture.Texture
import application.platform.mesh.GLVertex
import application.platform.mesh.GLVertexBuffer
import application.platform.texture.GLTexture2d

class GLTextParser : TextParser() {

    override fun createTextureBuffer(vertexName: String): VertexBuffer = GLVertexBuffer(
        vertex = GLVertex(
            name = vertexName,
            typeSize = 2
        ),
        data = textures.toFloatArray()
    )

    override fun createVertexBuffer(vertexName: String): VertexBuffer = GLVertexBuffer(
        vertex = GLVertex(
            name = vertexName,
            typeSize = 3
        ),
        data = vertices.toFloatArray()
    )

    override fun createTexture() : Texture {
        texture = GLTexture2d(uniformName = "")
        return texture
    }

}