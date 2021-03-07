package application.graphics.hud

import application.graphics.texture.Texture
import application.graphics.geometry.VertexBuffer
import java.nio.charset.Charset
import java.util.*

abstract class TextParser {

    private var columns = 15
    private var rows = 8

    companion object {
        private const val VERTICES_PER_QUAD = 4
    }

    protected val vertices = ArrayList<Float>()
    protected val textures = ArrayList<Float>()
    protected val indices = ArrayList<Int>()

    fun clean() {
        vertices.clear()
        textures.clear()
        indices.clear()
    }

    lateinit var texture: Texture

    abstract fun createTexture(): Texture

    private fun getAllAvailableChars(charsetName: String): String {
        val encoder = Charset.forName(charsetName).newEncoder()
        val result = StringBuilder()

        var c = 0.toChar()
        while (c < Character.MAX_VALUE) {
            if (encoder.canEncode(c)) {
                result.append(c)
            }
            c++
        }

        return result.toString()
    }

    fun parse(text: String) {
        val chars = text.toByteArray(Charset.forName("ISO-8859-1"))
        val numChars = chars.size

        val tileWidth = texture.width / columns.toFloat()
        val tileHeight = texture.height / rows.toFloat()

        clean()

        for (i in 0 until numChars) {
            val currChar = chars[i]
            val col: Int = currChar % columns
            val row: Int = currChar / columns

            // Build a character tile composed by two triangles

            // Left Top vertex
            vertices.add(i.toFloat() * tileWidth) // x
            vertices.add(0.0f) //y
            vertices.add(0f) //z
            textures.add(col.toFloat() / columns.toFloat())
            textures.add(row.toFloat() / rows.toFloat())
            indices.add(i * VERTICES_PER_QUAD)

            // Left Bottom vertex
            vertices.add(i.toFloat() * tileWidth) // x
            vertices.add(tileHeight) //y
            vertices.add(0f) //z
            textures.add(col.toFloat() / columns.toFloat())
            textures.add((row + 1).toFloat() / rows.toFloat())
            indices.add(i * VERTICES_PER_QUAD + 1)

            // Right Bottom vertex
            vertices.add(i.toFloat() * tileWidth + tileWidth) // x
            vertices.add(tileHeight) //y
            vertices.add(0f) //z
            textures.add((col + 1).toFloat() / columns.toFloat())
            textures.add((row + 1).toFloat() / rows.toFloat())
            indices.add(i * VERTICES_PER_QUAD + 2)

            // Right Top vertex
            vertices.add(i.toFloat() * tileWidth + tileWidth) // x
            vertices.add(0.0f) //y
            vertices.add(0f) //z
            textures.add((col + 1).toFloat() / columns.toFloat())
            textures.add(row.toFloat() / rows.toFloat())
            indices.add(i * VERTICES_PER_QUAD + 3)

            // Add indices por left top and bottom right vertices
            indices.add(i * VERTICES_PER_QUAD)
            indices.add(i * VERTICES_PER_QUAD + 2)
        }
    }

    fun getIndices(): IntArray = indices.toIntArray()

}