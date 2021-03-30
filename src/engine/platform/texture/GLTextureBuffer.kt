package engine.platform.texture

import engine.graphics.core.texture.TextureBuffer
import engine.graphics.core.texture.TextureData
import org.lwjgl.opengl.GL30.*
import java.nio.ByteBuffer

abstract class GLTextureBuffer(
    storagePath: String,
    detalization: Float = 0f,
    slot: Int = 0
) : TextureBuffer(
    storagePath = storagePath,
    detalization = detalization,
    slot = slot
) {

    override var id: Int = glGenTextures()

    protected abstract val type: Int

    override val faces: IntArray = intArrayOf(
        GL_TEXTURE_CUBE_MAP_POSITIVE_X,
        GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
        GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
        GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
        GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
        GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
    )

    override fun bind() {
        glBindTexture(type, id)
    }

    override fun activate() {
        glActiveTexture(GL_TEXTURE0 + slot)
    }

    override fun unbind() {
        glBindTexture(type, 0)
    }

    override fun onCreate() {
        glGenerateMipmap(type)
        glTexParameteri(type, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(type, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexParameteri(type, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(type, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameterf(type, GL_TEXTURE_LOD_BIAS, -detalization)
    }

    override fun create(textureSource: ByteBuffer?) {
        glTexImage2D(type, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, textureSource)
    }

    override fun createFace(textureData: TextureData, face: Int) {
        glTexImage2D(face, 0, GL_RGBA, textureData.width, textureData.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, textureData.sourceBuffer)
    }

    override fun createColor() {
        glTexImage2D(type, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null as ByteBuffer?)
    }

    override fun createDepth() {
        glTexImage2D(type, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, null as ByteBuffer?)
    }

    override fun onDestroy() {
        super.onDestroy()
        glDeleteTextures(id)
    }

}