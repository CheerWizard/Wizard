package engine.platform.core.frame

import engine.graphics.core.frame.RenderBuffer
import org.lwjgl.opengl.GL30.*

class GLRenderBuffer(
    renderWidth: Int,
    renderHeight: Int
) : RenderBuffer(
    renderWidth = renderWidth,
    renderHeight = renderHeight
) {

    override var id: Int = glGenRenderbuffers()

    override fun bind() {
        glBindRenderbuffer(GL_RENDERBUFFER, id)
        glRenderbufferStorage(
            GL_RENDERBUFFER,
            GL_DEPTH_COMPONENT,
            renderWidth,
            renderHeight
        )
    }

    override fun unbind() {
        glBindRenderbuffer(GL_RENDERBUFFER, 0)
    }

    override fun onDestroy() {
        glDeleteRenderbuffers(id)
    }

}