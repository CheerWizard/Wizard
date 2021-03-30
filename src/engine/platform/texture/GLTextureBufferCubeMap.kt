package engine.platform.texture

import org.lwjgl.opengl.GL13

class GLTextureBufferCubeMap(storagePath: String = STORAGE_PATH, slot: Int = 0) : GLTextureBuffer(storagePath = storagePath, slot = slot) {
    override val type: Int = GL13.GL_TEXTURE_CUBE_MAP
}