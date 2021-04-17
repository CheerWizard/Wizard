package imgui

class ScenePanel {

    interface Listener

    private var listener: Listener? = null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun removeListener() {
        this.listener = null
    }

    fun onUpdate() {
        ImGui.begin("Scene")
        ImGui.button("Add Entity Group")
        ImGui.sameLine()
        ImGui.button("Add Scene Component")
        ImGui.end()
    }

}