package engine.platform

import engine.core.ecs.Engine
import engine.core.tools.VideoCard
import engine.graphics.render.Render3dSystem
import engine.graphics.tools.ShaderFactory
import engine.platform.core.tools.GLShaderFactory
import engine.platform.core.tools.GLVideoCard
import engine.platform.renderer.GLRender3dSystem
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL30.*

class GLEngine(client: Client) : Engine(client = client) {

    override val shaderFactory: ShaderFactory = GLShaderFactory()
    override lateinit var videoCard: VideoCard

    override fun createLibraries() {
        println("Loading libraries...")

        System.setProperty("java.awt.headless", "true")

        GLFWErrorCallback.createPrint().set()
        check(GLFW.glfwInit()) { "Unable to initialize GLFW!" }

        println("Libraries loaded successfully!")
    }

    override fun enableDepthTest() {
        glEnable(GL_DEPTH_TEST)
    }

    override fun enableTransparency() {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    }

    override fun createRender3d(windowTitle: String) {
        val window = getNonNullWindow(windowTitle)
        window.requestSize()
        putSystem<Render3dSystem>(GLRender3dSystem(screenWidth = window.getWidth(), screenHeight = window.getHeight()))
    }

    override fun createVideoCard() {
        videoCard = GLVideoCard()
    }

}