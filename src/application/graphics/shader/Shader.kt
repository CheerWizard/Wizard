package application.graphics.shader

import application.core.Destroyable
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.system.exitProcess

abstract class Shader(private val fileName: String) : Destroyable {

    abstract var id: Int
    protected abstract val storagePath: String
    protected var source: CharSequence = ""

    fun readFile() : Shader {
        val source = StringBuilder()

        try {
            val reader = BufferedReader(FileReader("$storagePath/$fileName"))

            var line: String?
            while ((reader.readLine()).also { line = it } != null) {
                source.append(line).append("\n")
            }
            reader.close()
        } catch (e: IOException) {
            System.err.println("Error occurs during reading the file $fileName")
            e.printStackTrace()
            exitProcess(-1)
        } finally {
            this.source = source
        }

        return this
    }

    abstract fun onCreate()

}