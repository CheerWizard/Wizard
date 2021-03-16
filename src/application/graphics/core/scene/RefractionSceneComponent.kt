package application.graphics.core.scene

import application.core.ecs.SceneComponent
import application.graphics.core.frame.FrameBuffer
import application.graphics.core.texture.Texture
import application.graphics.shader.uniforms.UVector4f

class RefractionSceneComponent(
    var refractionFrameBuffer: FrameBuffer,
    var refractionColorTexture: Texture,
    var refractionDepthTexture: Texture,
    var clipping: UVector4f? = null
) : SceneComponent {

    override var isUpdated: Boolean = true

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