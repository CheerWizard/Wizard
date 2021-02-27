package application.graphics.texture

import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import java.io.IOException
import java.nio.ByteBuffer

class TextureData(fileName: String, storagePath: String) {

    var width: Int = 0
    var height: Int = 0
    var sourceBuffer: ByteBuffer? = null

    init {
        try {
            val memoryStack = MemoryStack.stackPush()
            val wb = memoryStack.mallocInt(1)
            val hb = memoryStack.mallocInt(1)
            val comp = memoryStack.mallocInt(1)

            sourceBuffer = STBImage.stbi_load("$storagePath/$fileName", wb, hb, comp, 4)
            width = wb.get()
            height = hb.get()

            if (sourceBuffer == null) {
                System.err.println(
                    """
                        Failed to load texture file path : $storagePath/$fileName
                        ${STBImage.stbi_failure_reason()}
                        """.trimIndent()
                )
            }
        } catch (e: IOException) {
            System.err.println("Error occurs during texture creation! Source path : $fileName")
            e.printStackTrace()
        }
    }

}