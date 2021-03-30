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
        with(ImGui) {
            begin("Scene")
            button("Add Entity Group")
            sameLine()
            button("Add Scene Component")
            end()
        }
    }

}