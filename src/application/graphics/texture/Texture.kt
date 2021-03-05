package application.graphics.texture

import application.core.Destroyable
import java.nio.ByteBuffer

abstract class Texture(
    private val storagePath: String = STORAGE_PATH,
    var slotUniformName: String,
    var strengthUniformName: String,
    val textureGrid: TextureGrid = TextureGrid(),
    val detalization: Float = 0f,
    var strength: Float = 1f
) : Destroyable {

    companion object {
        const val STORAGE_PATH = "res/textures"
        const val MAX_SLOTS = 8
    }

    abstract var id: Int

    var width: Int = 0
    var height: Int = 0

    var slot = 0

    protected abstract val faces: IntArray

    fun create(fileName: String) : Texture {
        createBufferData {
            val textureData = TextureData(fileName, storagePath)
            width = textureData.width
            height = textureData.height
            create(textureData.sourceBuffer)
            onCreate()
        }
        return this
    }

    protected abstract fun create(textureSource: ByteBuffer?)

    fun create(fileNames: Array<String>): Texture {
        createBufferData {
            for (i in fileNames.indices) {
                createFace(TextureData(fileNames[i], storagePath), faces[i])
            }
            onCreate()
        }
        return this
    }

    protected fun createBufferData(creationFunction : () -> Unit) {
        bind()
        creationFunction.invoke()
        deactivateTexture()
        unbind()
    }

    protected abstract fun createFace(textureData: TextureData, face: Int)

    fun createColor(width: Int, height: Int) : Texture {
        createBufferData {
            this.width = width
            this.height = height
            createColor()
            onCreate()
        }
        return this
    }

    fun createDepth(width: Int, height: Int) : Texture {
        createBufferData {
            this.width = width
            this.height = height
            createDepth()
            onCreate()
        }
        return this
    }

    protected abstract fun createColor()
    protected abstract fun createDepth()

    abstract fun bind()
    abstract fun unbind()
    protected abstract fun onCreate()

    abstract fun activateTexture()
    abstract fun deactivateTexture()

    override fun onDestroy() {
        unbind()
    }

}