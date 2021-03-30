package engine.graphics.core.texture

import engine.core.Destroyable
import java.nio.ByteBuffer

abstract class TextureBuffer(
    private val storagePath: String = STORAGE_PATH,
    val detalization: Float = 0f,
    var slot: Int = 0
) : Destroyable {

    companion object {
        const val STORAGE_PATH = "res/textures"
    }

    abstract var id: Int

    var width: Int = 0
    var height: Int = 0

    var isUpdated = true

    protected abstract val faces: IntArray

    fun create(fileName: String) : TextureBuffer {
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

    fun create(fileNames: Array<String>): TextureBuffer {
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
        unbind()
    }

    protected abstract fun createFace(textureData: TextureData, face: Int)

    fun createColor(width: Int, height: Int) : TextureBuffer {
        createBufferData {
            this.width = width
            this.height = height
            createColor()
            onCreate()
        }
        return this
    }

    fun createDepth(width: Int, height: Int) : TextureBuffer {
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
    abstract fun activate()

    protected abstract fun onCreate()

    override fun onDestroy() {
        unbind()
    }

}