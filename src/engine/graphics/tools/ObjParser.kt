package engine.graphics.tools

import engine.core.collection.Vector3fList
import org.joml.Vector2f
import org.joml.Vector3f
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class ObjParser {

    companion object {
        private const val STORAGE_PATH = "res/obj"
        private const val VERTEX_TOKEN = "v"
        private const val TEXTURE_TOKEN = "vt"
        private const val NORMAL_TOKEN = "vn"
        private const val INDEX_TOKEN = "f"
    }

    private var indices = IntArray(0)
    private var positions = FloatArray(0)
    private var textureCoordinates = FloatArray(0)
    private var normals = FloatArray(0)

    private val tempIndices = ArrayList<Int>()
    private val tempPositions = Vector3fList()
    private val tempTextureCoordinates = ArrayList<Vector2f>()
    private val tempNormals = Vector3fList()

    private fun clear() {
        indices = IntArray(0)
        positions = FloatArray(0)
        textureCoordinates = FloatArray(0)
        normals = FloatArray(0)
    }

    fun getIndices(): IntArray = indices

    fun getPositions(): FloatArray = positions

    fun getTextureCoordinates(): FloatArray = textureCoordinates

    fun getNormals(): FloatArray = normals

    fun getVertexCount(): Int = positions.size / 3

    fun parse(fileName: String) {
        clear()

        try {
            val reader = BufferedReader(FileReader("$STORAGE_PATH/$fileName.obj"))

            var line: String?
            while ((reader.readLine()).also { line = it } != null) {
                val tokens = (line as String).tokenize(' ')
                //todo consider how to handle non digit tokens
                if (tokens.size < 3) {
                    continue
                }

                when (tokens[0]) {
                    VERTEX_TOKEN -> tempPositions.add(
                        Vector3f(
                            tokens[1].toFloat(),
                            tokens[2].toFloat(),
                            tokens[3].toFloat()
                        )
                    )
                    TEXTURE_TOKEN -> tempTextureCoordinates.add(
                        Vector2f(
                            tokens[1].toFloat(),
                            tokens[2].toFloat()
                        )
                    )
                    NORMAL_TOKEN -> tempNormals.add(
                        Vector3f(
                            tokens[1].toFloat(),
                            tokens[2].toFloat(),
                            tokens[3].toFloat()
                        )
                    )
                    INDEX_TOKEN -> {
                        positions = tempPositions.toFloatArray()
                        textureCoordinates = FloatArray(tempPositions.size * 2)
                        normals = FloatArray(tempPositions.size * 3)
                        parseIndices(line as String)
                        break
                    }
                }
            }

            while ((reader.readLine()).also { line = it } != null) {
                parseIndices(line as String)
            }

            indices = tempIndices.toIntArray()

            reader.close()
        } catch (e: IOException) {
            System.err.println("Error occurs during reading the file $fileName")
            e.printStackTrace()
        } finally {
            clearTemporaryData()
        }
    }

    private fun clearTemporaryData() {
        tempIndices.clear()
        tempPositions.clear()
        tempTextureCoordinates.clear()
        tempNormals.clear()
    }

    private fun parseIndices(indicesLine: String) {
        val tokens = indicesLine.tokenize(' ')
        if (tokens.isNotEmpty() && tokens[0] != INDEX_TOKEN) return

        for (i in 1 until tokens.size) {
            val indices = tokenizeIndices(tokens[i], i + 1)
            val vertexIndex = indices[0].toInt() - 1
            val textureIndex = indices[1].toInt() - 1
            val normalIndex = indices[2].toInt() - 1

            this.tempIndices.add(vertexIndex)

            if (tempTextureCoordinates.isNotEmpty()) {
                val texture = tempTextureCoordinates[textureIndex]
                textureCoordinates[vertexIndex * 2] = texture.x
                textureCoordinates[vertexIndex * 2 + 1] = texture.y
            }

            if (tempNormals.isNotEmpty()) {
                val normal = tempNormals[normalIndex]
                normals[vertexIndex * 3] = normal.x
                normals[vertexIndex * 3 + 1] = normal.y
                normals[vertexIndex * 3 + 2] = normal.z
            }
        }
    }

    private fun tokenizeIndices(line: String, position: Int): List<String> {
        val tokens = line.tokenize('/')

        if (tokens.size == 2) {
            val lastElement = tokens.removeLast()
            tokens.add(position.toString())
            tokens.add(lastElement)
        }

        return tokens
    }

}

fun CharSequence.tokenize(removeChar: Char) : ArrayList<String> {
    val tokens = ArrayList<String>()
    var token = ""

    for (c in this) {
        if (c == removeChar) {
            if (token != "") {
                tokens.add(token)
            }
            token = ""
            continue
        }
        token += c
    }

    if (token != "") {
        tokens.add(token)
    }

    return tokens
}