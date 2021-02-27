package application.graphics.component

import application.core.ecs.Component

class SpecularComponent(
    var shiningUniformName: String,
    var reflectivityUniformName: String,
    var brightnessUniformName: String,
    var shining: Float = 1f,
    var reflectivity: Float = 1f,
    var brightness: Float = 0f,
) : Component {

    companion object {
        const val ID: Short = 7
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

}