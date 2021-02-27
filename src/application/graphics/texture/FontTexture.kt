package application.graphics.texture

import java.awt.Font

abstract class FontTexture(uniformName: String) : Texture(uniformSamplerName = uniformName) {

    fun parse(font: Font) {

    }

}