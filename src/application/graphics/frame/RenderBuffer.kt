package application.graphics.frame

import application.core.Destroyable

abstract class RenderBuffer(
    var renderWidth: Int,
    var renderHeight: Int
) : Destroyable {

    abstract var id: Int

    abstract fun bind()
    abstract fun unbind()

}