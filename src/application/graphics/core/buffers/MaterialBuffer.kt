package application.graphics.core.buffers

import application.core.Destroyable
import application.graphics.core.material.Material
import application.graphics.core.texture.Texture
import application.graphics.math.Color4f
import application.graphics.shader.Attribute

abstract class MaterialBuffer(
    var textureBuffer: Texture? = null,
    var coordinatesBuffer: VertexBuffer? = null,
    var colorsBuffer: VertexBuffer? = null,
) : Destroyable {

    abstract fun createCoordinatesBuffer(name: String)
    abstract fun createColorsBuffer(name: String)

    fun fillColor(color: Color4f, vertexStart: Int = 0, vertexEnd: Int) {
        colorsBuffer?.fill(vector4f = color, vertexStart = vertexStart, vertexEnd = vertexEnd)
    }

    fun addMaterial(material: Material) {
        material.textureCoordinates?.let {
            addCoordinates(it)
        }
        material.colors?.let {
            addColors(it)
        }
    }

    fun addCoordinates(coordinates: FloatArray) {
        coordinatesBuffer?.add(coordinates)
    }

    fun addColors(colors: FloatArray) {
        colorsBuffer?.add(colors)
    }

    fun prepare() {
        coordinatesBuffer?.prepare()
        colorsBuffer?.prepare()
    }

    fun enableAttributes() {
        coordinatesBuffer?.enableAttributes()
        colorsBuffer?.enableAttributes()
    }

    fun bind() {
        coordinatesBuffer?.bind()
        colorsBuffer?.bind()
    }

    fun unbind() {
        coordinatesBuffer?.unbind()
        colorsBuffer?.unbind()
    }

    fun disableAttributes() {
        coordinatesBuffer?.disableAttributes()
        colorsBuffer?.disableAttributes()
    }

    fun readMode() {
        coordinatesBuffer?.readMode()
        colorsBuffer?.readMode()
    }

    fun writeMode() {
        coordinatesBuffer?.writeMode()
        colorsBuffer?.writeMode()
    }

    override fun onDestroy() {
        coordinatesBuffer?.onDestroy()
        colorsBuffer?.onDestroy()
        textureBuffer?.onDestroy()
    }

    fun getAttributes(): ArrayList<Attribute> {
        return ArrayList<Attribute>().apply {
            coordinatesBuffer?.let {
                add(it.attribute)
            }
            colorsBuffer?.let {
                add(it.attribute)
            }
        }
    }

}