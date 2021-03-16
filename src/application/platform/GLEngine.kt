package application.platform

import application.core.ecs.Engine
import application.graphics.tools.ShaderFactory
import application.core.tools.VideoCard
import application.graphics.math.Color4f
import application.platform.core.tools.GLShaderFactory
import application.platform.core.tools.GLVideoCard
import application.platform.renderer.GLRender2dSystem
import application.platform.renderer.GLRender3dSystem
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30.*

class GLEngine(listener: Listener) : Engine(listener = listener) {

    override val shaderFactory: ShaderFactory = GLShaderFactory()
    override val videoCard: VideoCard = GLVideoCard()

    var clearColor: Color4f = Color4f.white()

    override fun onCreate() {
        val screenWidth = listener.getWidth()
        val screenHeight = listener.getHeight()

        putSystem(
            GLRender3dSystem(
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        )
        putSystem(
            GLRender2dSystem(
                screenWidth = screenWidth,
                screenHeight = screenHeight
            )
        )

        super.onCreate()
    }

    override fun createNativeCapabilities() {
        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w)
        enableTransparency()
    }

    override fun onUpdate() {
        clearDisplay()
        super.onUpdate()
    }

    private fun clearDisplay() {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun enableTransparency() {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    }

    fun disableBlending() {
        glDisable(GL_BLEND)
    }

}