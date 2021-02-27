package application.graphics.mesh

import application.core.Destroyable
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

abstract class VertexBuffer(
    val vertex: Vertex,
    data: FloatArray
    ) : Destroyable {

    abstract var id: Int

    val dataBuffer: FloatBuffer = BufferUtils.createFloatBuffer(data.size).apply {
        put(data)
        flip()
    }

    abstract fun bind()
    abstract fun unbind()

}