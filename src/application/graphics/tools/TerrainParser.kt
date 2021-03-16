package application.graphics.tools

import application.graphics.core.texture.TextureData
import org.joml.Vector3f
import java.util.*
import kotlin.math.abs

class TerrainParser {

    companion object {
        private const val STORAGE_PATH = "res/terrain"
        private const val X_LIMIT = 0.5f
        private const val Z_LIMIT = 0.5f
        private const val COLOR_LIMIT = 255 * 255 * 255
    }

    private lateinit var hMapTextureData: TextureData

    var gridCount: Int = 1
    var minY: Float = -0.5f
    var maxY: Float = 0.5f

    private val verticesList = ArrayList<Float>()
    private val texturesList = ArrayList<Float>()
    private val indicesList = ArrayList<Int>()

    protected lateinit var vertices: FloatArray
    protected lateinit var textures: FloatArray
    private lateinit var indices: IntArray
    protected lateinit var normals: FloatArray

    fun clear() {
        verticesList.clear()
        texturesList.clear()
        indicesList.clear()
        vertices = floatArrayOf()
        textures = floatArrayOf()
        indices = intArrayOf()
        normals = floatArrayOf()
    }

    fun getIndices(): IntArray = indices

    fun parseHeightMap(fileName: String) {
        clear()

        hMapTextureData = TextureData(fileName = fileName, storagePath = STORAGE_PATH)

        val hMapHeight = hMapTextureData.height
        val hMapWidth = hMapTextureData.width

        val incx: Float = abs(-X_LIMIT * 2) / (hMapWidth - 1)
        val incz: Float = abs(-Z_LIMIT * 2) / (hMapHeight - 1)

        for (row in 0 until hMapHeight) {
            for (column in 0 until hMapWidth) {
                // Create vertex for current position
                verticesList.add(-X_LIMIT + column * incx) // x
                val y = getY(x = column, z = row)
                verticesList.add(0f) //y
                verticesList.add(-Z_LIMIT + row * incz) //z

                // Set texture coordinates
                val gridCountFloat = gridCount.toFloat()
                texturesList.add(gridCountFloat * column.toFloat() / hMapWidth.toFloat())
                texturesList.add(gridCountFloat * row.toFloat() / hMapHeight.toFloat())

                // Create indices
                if (column < hMapWidth - 1 && row < hMapHeight - 1) {
                    val leftTop = row * hMapWidth + column
                    val leftBottom = (row + 1) * hMapWidth + column
                    val rightBottom = (row + 1) * hMapWidth + column + 1
                    val rightTop = row * hMapWidth + column + 1
                    indicesList.add(rightTop)
                    indicesList.add(leftBottom)
                    indicesList.add(leftTop)
                    indicesList.add(rightBottom)
                    indicesList.add(leftBottom)
                    indicesList.add(rightTop)
                }
            }
        }

        vertices = verticesList.toFloatArray()
        textures = texturesList.toFloatArray()
        indices = indicesList.toIntArray()
        normals = createNormals()
    }

    private fun getY(x: Int, z: Int) : Float {
        val hMapBuffer = hMapTextureData.sourceBuffer ?: return 0f
        val hMapWidth = hMapTextureData.width

        val r = hMapBuffer.get(x * 4 + 0 + z * 4 * hMapWidth).toInt()
        val g = hMapBuffer.get(x * 4 + 1 + z * 4 * hMapWidth).toInt()
        val b = hMapBuffer.get(x * 4 + 2 + z * 4 * hMapWidth).toInt()
        val a = hMapBuffer.get(x * 4 + 3 + z * 4 * hMapWidth).toInt()

        val color = ((0xFF and a) shl 24) or ((0xFF and r) shl 16) or ((0xFF and g) shl 8) or (0xFF and b)

        return minY + abs(maxY - minY) * (color.toFloat() / COLOR_LIMIT)
    }

    private fun createNormals(): FloatArray {
        val hMapWidth = hMapTextureData.width
        val hMapHeight = hMapTextureData.height

        val v0 = Vector3f()
        var v1 = Vector3f()
        var v2 = Vector3f()
        var v3 = Vector3f()
        var v4 = Vector3f()
        val v12 = Vector3f()
        val v23 = Vector3f()
        val v34 = Vector3f()
        val v41 = Vector3f()
        val normals = ArrayList<Float>()
        var normal = Vector3f()

        for (row in 0 until hMapHeight) {
            for (col in 0 until hMapWidth) {
                if (row > 0 && row < hMapHeight - 1 && col > 0 && col < hMapWidth - 1) {
                    val i0 = row * hMapWidth * 3 + col * 3
                    v0.x = vertices[i0]
                    v0.y = vertices[i0 + 1]
                    v0.z = vertices[i0 + 2]

                    val i1 = row * hMapWidth * 3 + (col - 1) * 3
                    v1.x = vertices[i1]
                    v1.y = vertices[i1 + 1]
                    v1.z = vertices[i1 + 2]
                    v1 = v1.sub(v0)

                    val i2 = (row + 1) * hMapWidth * 3 + col * 3
                    v2.x = vertices[i2]
                    v2.y = vertices[i2 + 1]
                    v2.z = vertices[i2 + 2]
                    v2 = v2.sub(v0)

                    val i3 = row * hMapWidth * 3 + (col + 1) * 3
                    v3.x = vertices[i3]
                    v3.y = vertices[i3 + 1]
                    v3.z = vertices[i3 + 2]
                    v3 = v3.sub(v0)

                    val i4 = (row - 1) * hMapWidth * 3 + col * 3
                    v4.x = vertices[i4]
                    v4.y = vertices[i4 + 1]
                    v4.z = vertices[i4 + 2]
                    v4 = v4.sub(v0)

                    v1.cross(v2, v12)
                    v12.normalize()
                    v2.cross(v3, v23)
                    v23.normalize()
                    v3.cross(v4, v34)
                    v34.normalize()
                    v4.cross(v1, v41)
                    v41.normalize()

                    normal = v12.add(v23).add(v34).add(v41)
                    normal.normalize()
                } else {
                    normal.x = 0f
                    normal.y = 1f
                    normal.z = 0f
                }
                normal.normalize()
                normals.add(normal.x)
                normals.add(normal.y)
                normals.add(normal.z)
            }
        }

        return normals.toFloatArray()
    }

}