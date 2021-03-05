package application.graphics.texture

import java.awt.Font

abstract class FontTexture(uniformName: String) : Texture(samplerUniformName = uniformName, strengthUniformName = "") {

    fun parse(font: Font) {

    }

}