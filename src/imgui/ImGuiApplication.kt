package imgui

import engine.Application
import engine.core.ecs.Engine
import engine.graphics.math.Colors
import engine.platform.GLEngine
import glm_.vec2.Vec2
import imgui.classes.Context
import imgui.impl.gl.ImplGL3
import imgui.impl.glfw.ImplGlfw

open class ImGuiApplication : Application(), ScenePanel.Listener {

    override val engine: Engine = GLEngine(title = "ImGui", client = this)

    private lateinit var imguiContext: Context
    private lateinit var imguiGl3: ImplGL3
    private lateinit var imguiGlfw: ImplGlfw

    private val scenePanel = ScenePanel()

    override fun onCreate() {
        super.onCreate()

        engine.window.enableFullScreen()

        imguiContext = Context()
        imguiGl3 = ImplGL3()
        imguiGlfw = ImplGlfw(engine.window.getWindow(), true)

        setStyle()
        setListeners()
    }

    private fun setStyle() {
        ImGui.style.apply {
            val bgColor = engine.backgroundColor
            val blueGrotto = Colors.fromHex("#0476D0")
            val tiffanyBlue = Colors.fromHex("#B4F5F0")
            val blueGrottoDark = Colors.fromHex("#0476D0", alpha = 50)

            colors[Col.WindowBg] (bgColor.x, bgColor.y, bgColor.z, bgColor.w)
            colors[Col.Button] (blueGrotto.x, blueGrotto.y, blueGrotto.z, blueGrotto.w)
            colors[Col.ButtonActive] (bgColor.x, bgColor.y, bgColor.z, bgColor.w)
            colors[Col.ButtonHovered] (blueGrottoDark.x, blueGrottoDark.y, blueGrottoDark.z, blueGrottoDark.w)
            colors[Col.Text] (tiffanyBlue.x, tiffanyBlue.y, tiffanyBlue.z, tiffanyBlue.w)
            colors[Col.Border] (blueGrotto.x, blueGrotto.y, blueGrotto.z, blueGrotto.w)

            windowTitleAlign = Vec2(0.5f, 0.5f)
            windowRounding = 2f
        }
    }

    private fun setListeners() {
        scenePanel.setListener(this)
    }

    override fun onUpdate() {
        newFrame()
        updateWidgets()
        render()
        super.onUpdate()
    }

    private fun newFrame() {
        imguiGl3.newFrame()
        imguiGlfw.newFrame()
        ImGui.newFrame()
    }

    private fun updateWidgets() {
        scenePanel.onUpdate()
    }

    private fun render() {
        ImGui.render()
        imguiGl3.renderDrawData(ImGui.drawData!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeListeners()
        imguiGl3.shutdown()
        imguiGlfw.shutdown()
        imguiContext.destroy()
    }

    private fun removeListeners() {
        scenePanel.removeListener()
    }

}