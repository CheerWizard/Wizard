package engine.graphics.core.buffers

import engine.core.Destroyable

abstract class VertexArray : Destroyable {

    abstract var id: Int

    abstract fun bind()
    abstract fun unbind()

}