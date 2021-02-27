package application.platform

import application.core.ecs.Engine
import application.graphics.math.Color4f
import application.platform.renderer.GLRender3dSystem
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30.*

class GLEngine(editor: Editor) : Engine(editor = editor) {

    var clearColor: Color4f = Color4f(
        red = 0.5f,
        blue = 0.5f,
        green = 0.5f,
        alpha = 1f,
    )

    override fun onCreate() {
        putSystem(
            GLRender3dSystem(
                screenWidth = editor.getWidth(),
                screenHeight = editor.getHeight()
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