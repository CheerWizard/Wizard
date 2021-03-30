package engine.graphics.shader

import engine.core.ecs.Component

class ShaderComponent(val shaderOwner: ShaderOwner) : Component {

    companion object {
        const val ID : Short = 2
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
        shaderOwner.onDestroy()
    }

}