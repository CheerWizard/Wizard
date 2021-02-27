package application.platform.texture

import application.graphics.texture.TextureGrid
import org.lwjgl.opengl.GL11

class GLTexture2d(
    storagePath: String = STORAGE_PATH,
    uniformName: String,
    textureGrid: TextureGrid = TextureGrid(),
    detalization: Float = 0f
) : GLTexture(
    storagePath = storagePath,
    uniformName = uniformName,
    textureGrid = textureGrid,
    detalization = detalization
) {
    override val type: Int = GL11.GL_TEXTURE_2D
}