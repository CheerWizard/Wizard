package imgui.io

import engine.io.IOController
import imgui.ImGuiIO
import imgui.flag.ImGuiKey
import imgui.internal.ImGui
import org.lwjgl.glfw.GLFW.*

class ImGuiIOController : IOController() {

    private lateinit var io: ImGuiIO

    private val mouseDownFlags = BooleanArray(5)

    override fun onCreate() {
        super.onCreate()
        io = ImGui.getIO()
    }

    override fun onKeyPressed(key: Int) {
        super.onKeyPressed(key)
        io.setKeysDown(key, true)

        io.keyCtrl = io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL)
        io.keyShift = io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT)
        io.keyAlt = io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT)
        io.keySuper = io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER)
    }

    override fun onKeyReleased(key: Int) {
        super.onKeyReleased(key)
        io.setKeysDown(key, false)
    }

    override fun onMousePressed(mouseButton: Int) {
        super.onMousePressed(mouseButton)

        mouseDownFlags[0] = mouseButton == GLFW_MOUSE_BUTTON_1
        mouseDownFlags[1] = mouseButton == GLFW_MOUSE_BUTTON_2
        mouseDownFlags[2] = mouseButton == GLFW_MOUSE_BUTTON_3
        mouseDownFlags[3] = mouseButton == GLFW_MOUSE_BUTTON_4
        mouseDownFlags[4] = mouseButton == GLFW_MOUSE_BUTTON_5

        io.setMouseDown(mouseDownFlags)

        if (!io.wantCaptureMouse && mouseDownFlags[1]) {
            ImGui.setWindowFocus(null)
        }
    }

    override fun onMouseReleased(mouseButton: Int) {
        super.onMouseReleased(mouseButton)

        for (i in mouseDownFlags.indices) {
            mouseDownFlags[i] = false
        }

        io.setMouseDown(mouseDownFlags)
    }

    override fun onMouseScrolled(xOffset: Double, yOffset: Double) {
        super.onMouseScrolled(xOffset, yOffset)
        io.mouseWheelH = io.mouseWheelH + xOffset.toFloat()
        io.mouseWheel = io.mouseWheel + yOffset.toFloat()
    }

    override fun onBind() {
        super.onBind()

        val keyMap = IntArray(ImGuiKey.COUNT)
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME
        keyMap[ImGuiKey.End] = GLFW_KEY_END
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER
        keyMap[ImGuiKey.A] = GLFW_KEY_A
        keyMap[ImGuiKey.C] = GLFW_KEY_C
        keyMap[ImGuiKey.V] = GLFW_KEY_V
        keyMap[ImGuiKey.X] = GLFW_KEY_X
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z
        io.setKeyMap(keyMap)
    }

}