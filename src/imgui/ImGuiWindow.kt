package imgui

import engine.core.tools.FpsTicker
import engine.io.IOController
import engine.window.Window
import imgui.flag.ImGuiMouseCursor
import imgui.gl3.ImGuiImplGl3
import imgui.io.ImGuiIOController
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_CURSOR
import org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL
import org.lwjgl.glfw.GLFW.glfwSetCursor
import org.lwjgl.glfw.GLFW.glfwSetInputMode
import java.awt.DisplayMode
import java.awt.GraphicsEnvironment

class ImGuiWindow(
    display: DisplayMode = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice.displayMode,
    title: String,
) : Window(
    display = display,
    title = title
) {

    private val glRenderer = ImGuiImplGl3()

    override val ioController: IOController = ImGuiIOController()

    private val mouseCursorPointers = LongArray(ImGuiMouseCursor.COUNT)

    private lateinit var io: ImGuiIO

    override fun onCreate() {
        super.onCreate()
        io = ImGui.getIO()
        glRenderer.init("#version 330 core")
    }

    override fun makeContextCurrent() {
        super.makeContextCurrent()
        ImGui.createContext()
    }

    fun updateCursor(cursorX: Float, cursorY: Float) {
        io.setMousePos(cursorX, cursorY)
    }

    fun newFrame() {
        requestSize()

        val displayWidth = getWidth()
        val displayHeight = getHeight()

        io.setDisplaySize(displayWidth, displayHeight)
        io.setDisplayFramebufferScale(1f, 1f)
        io.deltaTime = FpsTicker.getDeltaTimeSeconds()

        val imguiCursor = ImGui.getMouseCursor()
        glfwSetCursor(pointer, mouseCursorPointers[imguiCursor])
        glfwSetInputMode(pointer, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
    }

    fun render() {
        glRenderer.renderDrawData(ImGui.getDrawData())
    }

    override fun createIOController() {
        super.createIOController()
        mouseCursorPointers[ImGuiMouseCursor.Arrow] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
        mouseCursorPointers[ImGuiMouseCursor.TextInput] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR)
        mouseCursorPointers[ImGuiMouseCursor.ResizeAll] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
        mouseCursorPointers[ImGuiMouseCursor.ResizeNS] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR)
        mouseCursorPointers[ImGuiMouseCursor.ResizeEW] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR)
        mouseCursorPointers[ImGuiMouseCursor.ResizeNESW] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
        mouseCursorPointers[ImGuiMouseCursor.ResizeNWSE] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
        mouseCursorPointers[ImGuiMouseCursor.Hand] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR)
        mouseCursorPointers[ImGuiMouseCursor.NotAllowed] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR)
    }

    override fun onDestroy() {
        super.onDestroy()
        glRenderer.dispose()
        ImGui.destroyContext()
    }

}