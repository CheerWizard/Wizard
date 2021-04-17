package imgui

import imgui.tools.toolbar

class Toolbar {

    interface Listener {
        fun onExit()
        fun onImport()
        fun onExport()
    }

    private var listener: Listener? = null

    private val exitMenuItem = MenuItem(
        label = "Exit",
        listener = object : MenuItem.Listener {
            override fun onPressed() {
                listener?.onExit()
            }
        }
    )

    private val fileMenu: Menu = Menu(
        label = "File",
        subMenus = arrayOf(
            Menu(
                label = "New",
                items = arrayOf(
                    MenuItem(
                        label = "New Project"
                    ),
                    MenuItem(
                        label = "New Scene"
                    )
                )
            )
        ),
        items = arrayOf(
            MenuItem(
                label = "Import",
                listener = object : MenuItem.Listener {
                    override fun onPressed() {
                        listener?.onImport()
                    }
                }
            ),
            MenuItem(
                label = "Export",
                listener = object : MenuItem.Listener {
                    override fun onPressed() {
                        listener?.onExport()
                    }
                }
            ),
            exitMenuItem
        )
    )

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun removeListener() {
        listener = null
    }

    fun onUpdate() {
        toolbar {
            fileMenu.onUpdate()
        }
    }

}