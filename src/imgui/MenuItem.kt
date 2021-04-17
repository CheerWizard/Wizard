package imgui

class MenuItem(
    var label: String,
    var shortcut: String = "",
    var selected: Boolean = false,
    var enabled: Boolean = true,
    private var listener: Listener? = null
) {

    interface Listener {
        fun onPressed()
    }
    
    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun removeListener() {
        listener = null
    }

    fun onUpdate() {
        val pressed = ImGui.menuItem(label, shortcut, selected, enabled)
        if (pressed) {
            listener?.onPressed()
        }
    }
    
}