package imgui

import java.awt.FileDialog
import java.awt.Frame

class FileBrowser : Runnable {

    private val dialog = FileDialog(null as Frame?, "Select File to Open")

    private val files = ArrayList<String>()

    fun setImportMode() {
        dialog.run {
            title = "Import File"
            mode = FileDialog.LOAD
        }
    }

    fun setExportMode() {
        dialog.run {
            title = "Export File"
            mode = FileDialog.SAVE
        }
    }

    override fun run() {
        dialog.isVisible = true
    }

    fun createDir() {

    }

    fun removeDir() {

    }

    fun createFile() {

    }

    fun removeFile() {
        
    }

}