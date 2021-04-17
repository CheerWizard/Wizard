package imgui.tools

import imgui.ImGui

fun toolbar(block: () -> Unit) {
    if (ImGui.beginMainMenuBar()) {
        try {
            block()
        } finally {
            ImGui.endMainMenuBar()
        }
    }
}

fun menu(block: () -> Unit, menuLabel: String, menuEnabled: Boolean = true) {
    if (ImGui.beginMenu(menuLabel, menuEnabled)) {
        try {
            block()
        } finally {
            ImGui.endMenu()
        }
    }
}