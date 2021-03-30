package engine.graphics.core.texture

import engine.graphics.shader.uniforms.UInteger
import engine.graphics.shader.uniforms.UVector2f

class Texture(
    var textureGridId: Int = 0,
    var row: UInteger = UInteger(),
    var gridOffset: UVector2f = UVector2f()
) {

    init {
        apply()
    }

    fun apply() {
        val column = textureGridId % row.value
        val x = column / row.value.toFloat()
        val r = textureGridId / row.value
        val y = r / row.value.toFloat()

        gridOffset.data.run {
            this.x = x
            this.y = y
        }
        gridOffset.applyChanges()
    }

}