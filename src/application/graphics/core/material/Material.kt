package application.graphics.core.material

import application.graphics.math.Color4f

open class Material(
    var vertexCount: Int,
    open var colors: FloatArray? = null,
    open var textureCoordinates: FloatArray? = null,
    open var specular: Specular? = null,
    open var parallax: Parallax? = null
) {

    fun createColors(color: Color4f) {
        createColors()
        fillColor(color)
    }

    fun createColors() {
        colors = FloatArray(vertexCount * 4)
    }

    fun createCoordinates() {
        textureCoordinates = FloatArray(vertexCount * 2)
    }

    fun fillColor(color: Color4f) {
        colors?.let { c->
            var i = 0
            while (i < c.size) {
                c[i++] = color.x
                c[i++] = color.y
                c[i++] = color.z
                c[i++] = color.w
            }
        }
    }

}