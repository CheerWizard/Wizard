package application.graphics.texture

import application.core.Destroyable

abstract class Texture(
    private val storagePath: String = STORAGE_PATH,
    var uniformSamplerName: String,
    val textureGrid: TextureGrid = TextureGrid(),
    val detalization: Float = 0f
) : Destroyable {

    companion object {
        const val STORAGE_PATH = "res/textures"
    }

    abstract var id: Int

    var width: Int = 0
    var height: Int = 0

    var uniformSamplerValue = 0

    protected abstract val faces: IntArray

    fun create(fileName: String) : Texture {
        createData {
            val textureData = TextureData(fileName, storagePath)
            width = textureData.width
            height = textureData.height
            create(textureData)
            onCreate()
        }
        return this
    }

    protected abstract fun create(textureData: TextureData)

    fun create(fileNames: Array<String>): Texture {
        createData {
            for (i in fileNames.indices) {
                createFace(TextureData(fileNames[i], storagePath), faces[i])
            }
            onCreate()
        }
        return this
    }

    protected fun createData(creationFunction : () -> Unit) {
        onBind()
        creationFunction.invoke()
        deactivateTexture()
        onUnbind()
    }

    protected abstract fun createFace(textureData: TextureData, face: Int)

    abstract fun onBind()
    abstract fun onUnbind()
    abstract fun onCreate()

    abstract fun activateTexture()
    abstract fun deactivateTexture()

    override fun onDestroy() {
        onUnbind()
    }

}