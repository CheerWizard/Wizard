package application.graphics.geometry

import application.core.math.Vector2f
import application.core.math.Vector3f
import application.core.math.Vector4f
import application.graphics.math.Color4f
import application.graphics.texture.Texture

class Vertex(
    var position: Vector3f = Vector3f(),
    var color: Color4f = Color4f(),
    var textureCoordinate: Vector2f = Vector2f(),
    var normal: Vector3f = Vector3f(),
    var textureSlots: FloatArray = FloatArray(Texture.MAX_SLOTS)
) {
    companion object {
        const val MAX_SIZE = Vector4f.SIZE + Vector3f.SIZE * 2  + Vector2f.SIZE + Texture.MAX_SLOTS
    }
}