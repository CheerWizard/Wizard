package engine.graphics.core.frame

import engine.core.Destroyable

abstract class FrameBuffer(
    val displayWidth: Int,
    val displayHeight: Int,
    val frameWidth: Int,
    val frameHeight: Int
) : Destroyable {

    abstract var id: Int

    abstract fun bind()
    abstract fun bindFramePort()
    abstract fun bindDisplayPort()
    abstract fun draw()
    abstract fun attachColorTexture(textureId: Int)
    abstract fun attachDepthTexture(textureId: Int)
    abstract fun attachRenderBuffer(renderBufferId: Int)
    abstract fun unbind()

}