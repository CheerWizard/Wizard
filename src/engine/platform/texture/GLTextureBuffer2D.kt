package engine.platform.texture

import org.lwjgl.opengl.GL11

class GLTextureBuffer2D(
    storagePath: String = STORAGE_PATH,
    detalization: Float = 0f,
    slot: Int = 0
) : GLTextureBuffer(
    storagePath = storagePath,
    detalization = detalization,
    slot = slot
) {
    override val type: Int = GL11.GL_TEXTURE_2D
}