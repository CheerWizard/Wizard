package application.graphics.texture

import java.awt.Font

abstract class FontTexture(uniformName: String) : Texture(slotUniformName = uniformName, strengthUniformName = "") {

    fun parse(font: Font) {

    }

}