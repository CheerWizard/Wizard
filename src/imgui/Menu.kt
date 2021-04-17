package imgui

import imgui.tools.menu

class Menu(
    var label: String,
    var enabled: Boolean = true,
    val subMenus: Array<Menu> = emptyArray(),
    val items: Array<MenuItem> = emptyArray()
) {

    private val block : () -> Unit = {
        for (subMenu in subMenus) {
            subMenu.onUpdate()
        }
        for (item in items) {
            item.onUpdate()
        }
    }

    fun onUpdate() {
        menu(menuLabel = label, menuEnabled = enabled, block = block)
    }
}