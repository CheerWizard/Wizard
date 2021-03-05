package application.platform.texture

import application.graphics.texture.TextureGrid
import org.lwjgl.opengl.GL11

class GLTexture2d(
    storagePath: String = STORAGE_PATH,
    samplerUniformName: String,
    textureGrid: TextureGrid = TextureGrid(),
    detalization: Float = 0f,
    strengthUniformName: String,
    strength: Float = 1f
) : GLTexture(
    storagePath = storagePath,
    samplerUniformName = samplerUniformName,
    textureGrid = textureGrid,
    detalization = detalization,
    strengthUniformName = strengthUniformName,
    strength = strength
) {
    override val type: Int = GL11.GL_TEXTURE_2D
}