package application.platform.renderer

import application.graphics.render.Render2dSystem
import org.lwjgl.opengl.GL30.*

class GLRender2dSystem(screenHeight: Float, screenWidth: Float) : Render2dSystem(screenWidth = screenWidth, screenHeight = screenHeight) {

    override fun drawIndices(indexCount: Int) {
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
    }

    override fun drawVertices(vertexCount: Int) {
        glDrawArrays(GL_TRIANGLES, 0, vertexCount)
    }

    override fun onPolygonMode() {
        glPolygonMode(GL_FRONT_AND_BACK, polygonMode)
    }

    override fun onEnableCullFace() {
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
    }

    override fun onDisableCullFace() {
        glDisable(GL_CULL_FACE)
    }

    override fun onEnableClipping() {
        glEnable(GL_CLIP_DISTANCE0)
    }

    override fun onDisableClipping() {
        glDisable(GL_CLIP_DISTANCE0)
    }

}