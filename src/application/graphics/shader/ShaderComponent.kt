package application.graphics.shader

import application.core.ecs.Component

class ShaderComponent(val shaderOwner: ShaderOwner) : Component {

    override var isUpdated: Boolean = true

    companion object {
        const val ID : Short = 2
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
        shaderOwner.onDestroy()
    }

}