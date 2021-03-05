package application.graphics.scene

import application.core.ecs.SceneComponent
import application.graphics.frame.FrameBuffer
import application.graphics.frame.RenderBuffer
import application.graphics.texture.Texture
import application.graphics.uniform.UVector4f

class ReflectionSceneComponent(
    var reflectionFrameBuffer: FrameBuffer,
    var reflectionRenderBuffer: RenderBuffer,
    var reflectionColorTexture: Texture,
    var clipping: UVector4f? = null
) : SceneComponent {

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