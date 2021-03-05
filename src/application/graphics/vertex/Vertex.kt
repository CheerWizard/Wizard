package application.graphics.vertex

import application.core.math.Vector2f
import application.core.math.Vector3f
import application.graphics.math.Color4f

class Vertex(
    var position: Vector3f = Vector3f(),
    var color: Color4f = Color4f(),
    var textureCoordinate: Vector2f = Vector2f(),
    var normal: Vector3f = Vector3f(),
    var textureSlot: Float = 0f
)