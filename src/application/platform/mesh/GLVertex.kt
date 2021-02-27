package application.platform.mesh

import application.graphics.mesh.Vertex
import org.lwjgl.opengl.GL30

class GLVertex(
    name: String,
    typeSize: Int
) : Vertex(
    name = name,
    typeSize = typeSize
) {
    override fun disableAttribute() = GL30.glDisableVertexAttribArray(attribute)
    override fun enableAttribute() = GL30.glEnableVertexAttribArray(attribute)
}