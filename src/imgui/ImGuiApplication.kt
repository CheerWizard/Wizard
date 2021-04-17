package imgui

import engine.Application
import engine.core.ecs.Engine
import engine.graphics.math.Color4f
import engine.platform.GLEngine
import imgui.flag.ImGuiBackendFlags
import imgui.flag.ImGuiConfigFlags
import imgui.flag.ImGuiStyleVar
import imgui.flag.ImGuiWindowFlags
import imgui.type.ImBoolean
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import java.util.concurrent.Executors




open class ImGuiApplication : Application(),
    ScenePanel.Listener,
    Toolbar.Listener
{

    override val engine: Engine = GLEngine(client = this)

    private val scenePanel = ScenePanel()
    private val toolbar = Toolbar()

    private val ioExecutors = Executors.newSingleThreadExecutor()

    private val fileBrowser = FileBrowser()

    private lateinit var mainWindow : ImGuiWindow
//    private lateinit var sceneWindow : Window

    override fun onCreate() {
        super.onCreate()
        createWindows()
        prepareWindows()

//        engine.createRender3d(sceneWindow.title)

        setConfigs()
        setStyle()
        setListeners()
    }

    private fun setConfigs() {
        ImGui.getIO().apply {
            configFlags = ImGuiConfigFlags.NavEnableKeyboard
            configFlags = ImGuiConfigFlags.DockingEnable
//            configFlags = ImGuiConfigFlags.ViewportsEnable
            backendFlags = ImGuiBackendFlags.HasMouseCursors
            backendPlatformName = "imgui_java_impl_glfw"
        }
    }

    private fun createWindows() {
        mainWindow = engine.createImGuiWindow("Wizard")
//        sceneWindow = engine.createWindow("Scene")
    }

    private fun prepareWindows() {
//        sceneWindow.run {
//            setViewport()
//            setWindowListener(this@ImGuiApplication)
//            setCursorListener(this@ImGuiApplication)
//            disableVSync()
//        }
        mainWindow.run {
            backgroundColor = Color4f(
                red = 0.45f,
                green = 0.55f,
                blue = 0.6f,
                alpha = 1f
            )
            setViewport()
        }
    }

    override fun onBindIOController() {
        super.onBindIOController()

        mainWindow.setCursorListener(this)
        mainWindow.setWindowListener(this)

        mainWindow.ioController.run {
            bindKeyPressedEvent(GLFW_KEY_ESCAPE) { onExit() }
        }

//        sceneWindow.ioController.run {
//            bindKeyPressedEvent(GLFW.GLFW_KEY_V) { sceneWindow.toggleVSync() }
//        }
    }

    private fun setStyle() {
        ImGui.styleColorsDark()
    }

    private fun setListeners() {
        scenePanel.setListener(this)
        toolbar.setListener(this)
    }

    override fun onUpdate() {
        newFrame()
        beginDockSpace()
        updateWidgets()
        endDockSpace()
        render()
    }

    private fun endDockSpace() {
        ImGui.end()
    }

    private fun beginDockSpace() {
//        val mainViewport = ImGui.getMainViewport()
//        ImGui.setNextWindowPos(mainViewport.workPosX, mainViewport.workPosY)
//        ImGui.setNextWindowSize(mainViewport.workSizeX, mainViewport.workSizeY)
//        ImGui.setNextWindowViewport(mainViewport.id)

        var windowFlags = ImGuiWindowFlags.MenuBar or ImGuiWindowFlags.NoDocking

        mainWindow.requestSize()
        ImGui.setNextWindowPos(0.0f, 0.0f)
        ImGui.setNextWindowSize(mainWindow.getWidth(), mainWindow.getHeight())

        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0f)
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0f)

        windowFlags = windowFlags or
                ImGuiWindowFlags.NoTitleBar or
                ImGuiWindowFlags.NoCollapse or
                ImGuiWindowFlags.NoResize or
                ImGuiWindowFlags.NoMove or
                ImGuiWindowFlags.NoBringToFrontOnFocus or
                ImGuiWindowFlags.NoNavFocus

        ImGui.begin("DockSpace", ImBoolean(true), windowFlags)
        ImGui.popStyleVar(2)

        ImGui.dockSpace(ImGui.getID("DockSpace"))
    }

    private fun newFrame() {
        mainWindow.newFrame()
        ImGui.newFrame()
    }

    private fun updateWidgets() {
        toolbar.onUpdate()
        scenePanel.onUpdate()
        ImGui.showDemoWindow()
    }

    private fun render() {
        ImGui.render()
        mainWindow.render()
    }

    override fun onCursorCoordinatesChanged(x: Float, y: Float) {
        super.onCursorCoordinatesChanged(x, y)
        mainWindow.updateCursor(cursorX = x, cursorY = y)
    }

    override fun onDestroy() {
        super.onDestroy()
        ioExecutors.shutdown()
        removeListeners()
    }

    private fun removeListeners() {
        scenePanel.removeListener()
        toolbar.removeListener()
    }

    override fun onExit() {
        mainWindow.close()
    }

    override fun onImport() {
        ioExecutors.execute(fileBrowser.apply {
            setImportMode()
        })
    }

    override fun onExport() {
        ioExecutors.execute(fileBrowser.apply {
            setExportMode()
        })
    }

    override fun createMaxFps(): Long = mainWindow.getRefreshRate().toLong()

    override fun isOpen(): Boolean = mainWindow.isOpen()

}