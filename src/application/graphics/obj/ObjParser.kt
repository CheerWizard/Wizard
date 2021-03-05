package application.graphics.obj

import application.core.collection.Vector3fList
import application.core.math.Vector2f
import application.core.math.Vector3f
import application.graphics.geometry.VertexBuffer
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.system.exitProcess

abstract class ObjParser {

    companion object {
        private const val STORAGE_PATH = "res/obj"
        private const val VERTEX_TOKEN = "v"
        private const val TEXTURE_TOKEN = "vt"
        private const val NORMAL_TOKEN = "vn"
        private const val INDEX_TOKEN = "f"
    }

    private val indices = ArrayList<Int>()
    protected val vertices = Vector3fList()
    private val textures = ArrayList<Vector2f>()
    private val normals = Vector3fList()

    protected var textureArray = FloatArray(0)
    protected var normalsArray = FloatArray(0)
    protected var tangentsArray = FloatArray(0)

    private fun clean() {
        indices.clear()
        vertices.clear()
        textures.clear()
        normals.clear()
    }

    fun parse(fileName: String) {
        clean()

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
                    VERTEX_TOKEN -> vertices.add(
                        Vector3f(
                            x = tokens[1].toFloat(),
                            y = tokens[2].toFloat(),
                            z = tokens[3].toFloat()
                        )
                    )
                    TEXTURE_TOKEN -> textures.add(
                        Vector2f(
                            x = tokens[1].toFloat(),
                            y = tokens[2].toFloat()
                        )
                    )
                    NORMAL_TOKEN -> normals.add(
                        Vector3f(
                            x = tokens[1].toFloat(),
                            y = tokens[2].toFloat(),
                            z = tokens[3].toFloat()
                        )
                    )
                    INDEX_TOKEN -> {
                        textureArray = FloatArray(vertices.size * 2)
                        normalsArray = FloatArray(vertices.size * 3)
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
        }
    }

    fun getIndices(): IntArray = indices.toIntArray()

    abstract fun createVertexBuffer(name: String): VertexBuffer
    abstract fun createTextureBuffer(name: String): VertexBuffer
    abstract fun createNormalBuffer(name: String): VertexBuffer
    abstract fun createTangentsBuffer(name: String): VertexBuffer

    private fun parseIndices(indicesLine: String) {
        val tokens = indicesLine.tokenize(' ')
        if (tokens.isNotEmpty() && tokens[0] != INDEX_TOKEN) return

        for (i in 1 until tokens.size) {
            val indices = tokenizeIndices(tokens[i], i + 1)
            val vertexIndex = indices[0].toInt() - 1
            val textureIndex = indices[1].toInt() - 1
            val normalIndex = indices[2].toInt() - 1

            this.indices.add(vertexIndex)

            if (textures.isNotEmpty()) {
                val texture = textures[textureIndex]
                textureArray[vertexIndex * 2] = texture.x
                textureArray[vertexIndex * 2 + 1] = texture.y
            }

            if (normals.isNotEmpty()) {
                val normal = normals[normalIndex]
                normalsArray[vertexIndex * 3] = normal.x
                normalsArray[vertexIndex * 3 + 1] = normal.y
                normalsArray[vertexIndex * 3 + 2] = normal.z
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