package application.platform.window

import application.window.Cursor
import org.lwjgl.glfw.GLFW

class GLCursor : Cursor() {
    override fun updateCoordinates() = GLFW.glfwGetCursorPos(windowId, xCoordinates, yCoordinates)
}