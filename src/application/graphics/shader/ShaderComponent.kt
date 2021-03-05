package application.graphics.shader

import application.core.ecs.Component

class ShaderComponent(val shaderOwner: ShaderOwner) : Component {

    companion object {
        const val ID : Short = 1
    }

    fun startShader() {
        shaderOwner.onStart()
    }

    fun stopShader() {
        shaderOwner.onStop()
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
        shaderOwner.onDestroy()
    }

}