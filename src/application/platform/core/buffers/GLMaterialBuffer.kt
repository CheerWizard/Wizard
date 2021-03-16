package application.platform.core.buffers

import application.graphics.core.buffers.MaterialBuffer
import application.graphics.shader.Attribute2f
import application.graphics.shader.Attribute4f

class GLMaterialBuffer : MaterialBuffer() {

    override fun createColorsBuffer(name: String) {
        colorsBuffer = GLVertexBuffer(attribute = Attribute4f(
            name = name
        ))
    }

    override fun createCoordinatesBuffer(name: String) {
        coordinatesBuffer = GLVertexBuffer(attribute = Attribute2f(
            name = name
        ))
    }

}