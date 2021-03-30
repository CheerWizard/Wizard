package engine.platform.window

import engine.window.Cursor
import org.lwjgl.glfw.GLFW

class GLCursor : Cursor() {
    override fun updateCoordinates() = GLFW.glfwGetCursorPos(windowId, xCoordinates, yCoordinates)
}