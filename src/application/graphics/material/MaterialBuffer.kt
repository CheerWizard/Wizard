package application.graphics.material

import application.core.Destroyable
import application.graphics.geometry.VertexBuffer
import application.graphics.texture.Texture

class MaterialBuffer(
        var textureBuffer: Texture,
        var coordinatesBuffer: VertexBuffer,
        var colorsBuffer: VertexBuffer? = null,
) : Destroyable {

    fun addCoordinates(coordinates: FloatArray) {
        coordinatesBuffer.add(coordinates)
    }

    fun addColors(colors: FloatArray) {
        colorsBuffer?.add(colors)
    }

    fun prepare() {
        coordinatesBuffer.prepare()
        colorsBuffer?.prepare()
    }

    fun enableAttributes() {
        coordinatesBuffer.enableAttributes()
        colorsBuffer?.enableAttributes()
    }

    fun bind() {
        coordinatesBuffer.bind()
        colorsBuffer?.bind()
    }

    fun unbind() {
        coordinatesBuffer.unbind()
        colorsBuffer?.unbind()
    }

    fun disableAttributes() {
        coordinatesBuffer.disableAttributes()
        colorsBuffer?.disableAttributes()
    }

    override fun onDestroy() {
        coordinatesBuffer.onDestroy()
        colorsBuffer?.onDestroy()
        textureBuffer.onDestroy()
    }

}