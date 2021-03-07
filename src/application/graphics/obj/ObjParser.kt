package application.graphics.obj

import application.core.collection.Vector3fList
import application.core.math.Vector2f
import application.core.math.Vector3f
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.system.exitProcess

class ObjParser {

    companion object {
        private const val STORAGE_PATH = "res/obj"
        private const val VERTEX_TOKEN = "v"
        private const val TEXTURE_TOKEN = "vt"
        private const val NORMAL_TOKEN = "vn"
        private const val INDEX_TOKEN = "f"
    }

    private val indices = ArrayList<Int>()
    private val positions = Vector3fList()
    private var textureCoordinates = FloatArray(0)
    private var normals = FloatArray(0)

    private val tempTextureCoordinates = ArrayList<Vector2f>()
    private val tempNormals = Vector3fList()

    private fun clear() {
        indices.clear()
        positions.clear()
        textureCoordinates = FloatArray(0)
        normals = FloatArray(0)
    }

    fun getIndices(): IntArray = indices.toIntArray()

    fun getPositions(): Vector3fList = positions

    fun getTextureCoordinates(): FloatArray = textureCoordinates

    fun getNormals(): FloatArray = normals

    fun parse(fileName: String) {
        clear()

        try {
            val reader = BufferedReader(FileReader("$STORAGE_PATH/$fileName"))

            var line: String?
            while ((reader.readLine()).also { line = it } != null) {
                val tokens = (line as String).tokenize(' ')
                //todo consider how to handle non digit tokens
                if (tokens.size < 3) {
                    continue
                }

                when (tokens[0]) {
                    VERTEX_TOKEN -> positions.add(
                        Vector3f(
                            x = tokens[1].toFloat(),
                            y = tokens[2].toFloat(),
                            z = tokens[3].toFloat()
                        )
                    )
                    TEXTURE_TOKEN -> tempTextureCoordinates.add(
                        Vector2f(
                            x = tokens[1].toFloat(),
                            y = tokens[2].toFloat()
                        )
                    )
                    NORMAL_TOKEN -> tempNormals.add(
                        Vector3f(
                            x = tokens[1].toFloat(),
                            y = tokens[2].toFloat(),
                            z = tokens[3].toFloat()
                        )
                    )
                    INDEX_TOKEN -> {
                        textureCoordinates = FloatArray(positions.size * 2)
                        normals = FloatArray(positions.size * 3)
                        parseIndices(line as String)
                        break
                    }
                }
            }

            while ((reader.readLine()).also { line = it } != null) {
                parseIndices(line as String)
            }

            reader.close()
        } catch (e: IOException) {
            System.err.println("Error occurs during reading the file $fileName")
            e.printStackTrace()
            exitProcess(-1)
        } finally {
            clearTemporaryData()
        }
    }

    private fun clearTemporaryData() {
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

            this.indices.add(vertexIndex)

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