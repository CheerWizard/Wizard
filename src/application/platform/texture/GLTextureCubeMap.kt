package application.platform.texture

import org.lwjgl.opengl.GL13

class GLTextureCubeMap(storagePath: String = STORAGE_PATH) : GLTexture(storagePath = storagePath, strengthUniformName = "") {
    override val type: Int = GL13.GL_TEXTURE_CUBE_MAP
}