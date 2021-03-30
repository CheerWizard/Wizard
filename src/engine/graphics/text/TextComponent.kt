package engine.graphics.text

import engine.core.ecs.Component

class TextComponent(var text: String) : Component {

    companion object {
        const val ID: Short = 8
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

    fun apply() {

    }

}