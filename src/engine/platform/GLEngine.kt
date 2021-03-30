package engine.platform

import engine.core.ecs.Engine
import engine.core.tools.VideoCard
import engine.graphics.tools.ShaderFactory
import engine.platform.core.tools.GLShaderFactory
import engine.platform.core.tools.GLVideoCard
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30.*

class GLEngine(title: String, client: Client) : Engine(title = title, client = client) {

    override val shaderFactory: ShaderFactory = GLShaderFactory()
    override lateinit var videoCard: VideoCard

    override fun createLibraries() {
        println("Loading libraries...")

        System.setProperty("java.awt.headless", "true")

        GLFWErrorCallback.createPrint().set()
        check(GLFW.glfwInit()) { "Unable to initialize GLFW!" }

        window.run {
            onCreate()
            disableVSync()
        }

        GL.createCapabilities()
        glEnable(GL_DEPTH_TEST)
        enableTransparency()

        videoCard = GLVideoCard()

        println("Libraries loaded successfully!")
    }

    override fun clearDisplay() {
        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, backgroundColor.w)
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