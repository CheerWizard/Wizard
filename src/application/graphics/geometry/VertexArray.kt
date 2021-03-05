package application.graphics.geometry

import application.core.Destroyable

abstract class VertexArray : Destroyable {

    abstract var id: Int

    abstract fun bind()
    abstract fun unbind()

}