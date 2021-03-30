package engine.platform.renderer

import engine.graphics.render.Render3dSystem
import org.lwjgl.opengl.GL40.*

class GLRender3dSystem(screenWidth: Float, screenHeight: Float) : Render3dSystem(screenWidth = screenWidth, screenHeight = screenHeight) {

    override fun drawElements(indexCount: Int) {
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
    }

    override fun drawElements(indexCount: Int, instanceCount: Int) {
        glDrawElementsInstanced(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0, instanceCount)
    }

    override fun drawArrays(vertexCount: Int) {
        glDrawArrays(GL_TRIANGLES, 0 , vertexCount)
    }

    override fun drawArrays(vertexCount: Int, instanceCount: Int) {
        glDrawArraysInstanced(GL_TRIANGLES, 0, vertexCount, instanceCount)
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