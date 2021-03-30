package engine.graphics.core.scene

import engine.core.ecs.SceneComponent
import engine.graphics.core.frame.FrameBuffer
import engine.graphics.core.texture.TextureBuffer
import engine.graphics.shader.uniforms.UVector4f

class RefractionSceneComponent(
    var refractionFrameBuffer: FrameBuffer,
    var refractionColorTexture: TextureBuffer,
    var refractionDepthTexture: TextureBuffer,
    var clipping: UVector4f? = null
) : SceneComponent {

    companion object {
        const val ID : Short = 2
        const val DEFAULT_REFRACTION_WIDTH = 1280
        const val DEFAULT_REFRACTION_HEIGHT = 720
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
        refractionFrameBuffer.onDestroy()
        refractionColorTexture.onDestroy()
        refractionDepthTexture.onDestroy()
    }

    fun create() : RefractionSceneComponent {
        refractionFrameBuffer.run {
            bind()
            draw()
            attachColorTexture(refractionColorTexture.id)
            attachDepthTexture(refractionDepthTexture.id)
            unbind()
        }
        return this
    }

    fun bind() {
        refractionColorTexture.unbind()
        refractionDepthTexture.unbind()
        refractionFrameBuffer.bind()
        refractionFrameBuffer.bindFramePort()
    }

    fun unbind() {
        refractionFrameBuffer.unbind()
    }

}