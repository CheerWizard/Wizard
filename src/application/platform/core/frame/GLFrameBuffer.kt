package application.platform.core.frame

import application.graphics.core.frame.FrameBuffer
import org.lwjgl.opengl.GL32.*

class GLFrameBuffer(
    displayWidth: Int,
    displayHeight: Int,
    frameWidth: Int,
    frameHeight: Int
) : FrameBuffer(
    displayWidth = displayWidth,
    displayHeight = displayHeight,
    frameWidth = frameWidth,
    frameHeight = frameHeight
) {

    override var id: Int = glGenFramebuffers()

    override fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id)
    }

    override fun bindFramePort() {
        glViewport(0, 0, frameWidth, frameHeight)
    }

    override fun bindDisplayPort() {
        glViewport(0, 0, displayWidth, displayHeight)
    }

    override fun draw() {
        glDrawBuffer(GL_COLOR_ATTACHMENT0)
    }

    override fun attachColorTexture(textureId: Int) {
        glFramebufferTexture(
            GL_FRAMEBUFFER,
            GL_COLOR_ATTACHMENT0,
            textureId,
            0
        )
    }

    override fun attachDepthTexture(textureId: Int) {
        glFramebufferTexture(
            GL_FRAMEBUFFER,
            GL_DEPTH_ATTACHMENT,
            textureId,
            0
        )
    }

    override fun attachRenderBuffer(renderBufferId: Int) {
        glFramebufferRenderbuffer(
            GL_FRAMEBUFFER,
            GL_DEPTH_ATTACHMENT,
            GL_RENDERBUFFER,
            renderBufferId
        )
    }

    override fun unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        bindDisplayPort()
    }

    override fun onDestroy() {
        glDeleteFramebuffers(id)
    }


}