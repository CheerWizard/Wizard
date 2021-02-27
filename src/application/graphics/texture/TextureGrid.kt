package application.graphics.texture

import application.core.math.Vector2f

class TextureGrid(
    var textureGridId: Int = 0,
    var rowCountName: String = "",
    var rowCountValue: Int = 1,
    var gridOffsetName: String = "",
) {
    var gridOffset: Vector2f = Vector2f()

    init {
        apply()
    }

    fun apply() {
        val column = textureGridId % rowCountValue
        val x = column / rowCountValue.toFloat()
        val row = textureGridId / rowCountValue
        val y = row / rowCountValue.toFloat()

        gridOffset.x = x
        gridOffset.y = y
    }

}