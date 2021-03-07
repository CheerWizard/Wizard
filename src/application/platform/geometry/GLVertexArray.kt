package application.platform.geometry

import application.graphics.geometry.VertexArray
import org.lwjgl.opengl.GL30.*

class GLVertexArray : VertexArray() {

    override var id: Int = glGenVertexArrays()

    override fun bind() {
        glBindVertexArray(id)
    }

    override fun unbind() {
        glBindVertexArray(0)
    }

    override fun onDestroy() {
        glDeleteVertexArrays(id)
    }

}