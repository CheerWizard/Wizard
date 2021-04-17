package engine.window

import org.joml.Vector2i
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.system.MemoryStack

class Monitor(
    var pointer: Long,
    var position: Vector2i,
    var size: Vector2i,
    var refreshRate: Int
)

object Monitors {

    private var monitor: Monitor? = null
    
    fun getMonitor(): Monitor {
        if (monitor == null) {
            return newMonitor()
        }
        return monitor as Monitor
    }

    fun newMonitor(refreshRate: Int = GLFW_DONT_CARE) : Monitor {
        val primaryMonitor = glfwGetPrimaryMonitor()

        val posX = MemoryStack.stackMallocInt(1)
        val posY = MemoryStack.stackMallocInt(1)
        val width = MemoryStack.stackMallocInt(1)
        val height = MemoryStack.stackMallocInt(1)

        glfwGetMonitorPos(primaryMonitor, posX, posY)
        glfwGetMonitorPhysicalSize(primaryMonitor, width, height)

        val position = Vector2i(posX.get(), posY.get())
        val size = Vector2i(width.get(), height.get())

        monitor = Monitor(
            pointer = primaryMonitor,
            position = position,
            size = size,
            refreshRate = refreshRate
        )

        return monitor as Monitor
    }
    
}