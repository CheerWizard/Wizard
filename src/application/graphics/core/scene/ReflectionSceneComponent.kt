package application.graphics.core.scene

import application.core.ecs.SceneComponent
import application.graphics.core.frame.FrameBuffer
import application.graphics.core.frame.RenderBuffer
import application.graphics.core.texture.Texture
import application.graphics.shader.uniforms.UVector4f

class ReflectionSceneComponent(
    var reflectionFrameBuffer: FrameBuffer,
    var reflectionRenderBuffer: RenderBuffer,
    var reflectionColorTexture: Texture,
    var clipping: UVector4f? = null
) : SceneComponent {

    override var isUpdated: Boolean = true

    companion object {
        const val ID : Short = 1
        const val DEFAULT_REFLECTION_WIDTH = 320
        const val DEFAULT_REFLECTION_HEIGHT = 180
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
        reflectionFrameBuffer.onDestroy()
        reflectionRenderBuffer.onDestroy()
        reflectionColorTexture.onDestroy()
    }

    fun create() : ReflectionSceneComponent {
        reflectionFrameBuffer.run {
            bind()
            draw()
            attachColorTexture(reflectionColorTexture.id)
            attachRenderBuffer(reflectionRenderBuffer.id)
            unbind()
        }
        return this
    }

    fun bind() {
        reflectionColorTexture.unbind()
        reflectionFrameBuffer.bind()
        reflectionFrameBuffer.bindFramePort()
    }

    fun unbind() {
        reflectionFrameBuffer.unbind()
    }

}