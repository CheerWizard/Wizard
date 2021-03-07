package application.platform.hud

import application.graphics.hud.HudSystem
import org.lwjgl.opengl.GL30.*

class GLHudSystem(screenHeight: Float, screenWidth: Float) : HudSystem(screenHeight = screenHeight, screenWidth = screenWidth) {

    override fun draw(indexCount: Int) {
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
    }

    override fun onDrawVertices(vertexCount: Int) {
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)
    }

    override fun setPolygonMode(polygonMode: Int) {
        super.setPolygonMode(polygonMode)
        glPolygonMode(GL_FRONT_AND_BACK, polygonMode)
    }

    override fun enableCullFace() {
        super.enableCullFace()
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
    }

    override fun disableCullFace() {
        super.disableCullFace()
        glDisable(GL_CULL_FACE)
    }

}