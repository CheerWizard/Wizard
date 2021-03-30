package engine.graphics.tools

import engine.core.collection.Vector2fList
import engine.core.collection.Vector3fList
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

class FntParser {

    companion object {
        private const val STORAGE_PATH = "res/fonts"
    }

    private var indices = IntArray(0)
    private var positions = FloatArray(0)
    private var textureCoordinates = FloatArray(0)

    protected val tempPositions = Vector3fList()
    protected val tempTextureCoordinates = Vector2fList()
    protected val tempIndices = ArrayList<Int>()

    fun clear() {
        indices = IntArray(0)
        positions = FloatArray(0)
        textureCoordinates = FloatArray(0)
    }

    private fun clearTemporaryData() {
        tempIndices.clear()
        tempPositions.clear()
        tempTextureCoordinates.clear()
    }

    fun parse(fileName: String) {
        clear()

        try {
            val reader = BufferedReader(FileReader("$STORAGE_PATH/$fileName.fnt"))

            var line: String?
            while ((reader.readLine()).also { line = it } != null) {

            }

        } catch (e: IOException) {
            System.err.println("Error occurs during reading the file $fileName")
            e.printStackTrace()
        } finally {
            clearTemporaryData()
        }
    }

}