package application.graphics.component

import application.core.ecs.Component
import application.graphics.math.Color4f

class ColorComponent(
    val colorName: String,
    val color: Color4f
) : Component {

    companion object {
        const val ID: Short = 5
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

}