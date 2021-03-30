package engine.graphics.core.mesh

import engine.graphics.shader.uniforms.UColor4f
import engine.graphics.shader.uniforms.UVector2f
import engine.graphics.shader.uniforms.UVector3f

class Vertex(
    var position: UVector3f = UVector3f(),
    var color: UColor4f = UColor4f(),
    var textureCoordinate: UVector2f? = null,
    var normal: UVector3f? = null
) {
    fun getSize(): Int = 8
}
