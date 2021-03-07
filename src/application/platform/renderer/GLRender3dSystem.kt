package application.platform.renderer

import application.graphics.render.Render3dSystem
import org.lwjgl.opengl.GL30.*

class GLRender3dSystem(screenWidth: Float, screenHeight: Float) : Render3dSystem(screenWidth = screenWidth, screenHeight = screenHeight) {

    override fun draw(indexCount: Int) {
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
    }

    override fun onDrawVertices(vertexCount: Int) {
        glDrawArrays(GL_TRIANGLES, 0 , vertexCount)
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

    override fun enableClipping() {
        super.enableClipping()
        glEnable(GL_CLIP_DISTANCE0)
    }

    override fun disableClipping() {
        super.disableClipping()
        glDisable(GL_CLIP_DISTANCE0)
    }

}