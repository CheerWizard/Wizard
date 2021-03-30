package engine.graphics.core.scene

import engine.core.ecs.SceneComponent
import engine.graphics.core.frame.FrameBuffer
import engine.graphics.core.frame.RenderBuffer
import engine.graphics.core.texture.TextureBuffer
import engine.graphics.shader.uniforms.UVector4f

class ReflectionSceneComponent(
    var reflectionFrameBuffer: FrameBuffer,
    var reflectionRenderBuffer: RenderBuffer,
    var reflectionColorTexture: TextureBuffer,
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