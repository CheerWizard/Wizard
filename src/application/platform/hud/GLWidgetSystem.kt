package application.platform.hud

import application.graphics.widget.WidgetSystem
import org.lwjgl.opengl.GL30.*

class GLWidgetSystem(screenHeight: Float, screenWidth: Float) : WidgetSystem(screenHeight = screenHeight, screenWidth = screenWidth) {

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