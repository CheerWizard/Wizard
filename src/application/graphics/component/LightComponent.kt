package application.graphics.component

import application.core.ecs.Component
import application.core.math.Vector3f
import application.graphics.math.Color3f
import application.graphics.math.Translator3f

class LightComponent(
    var positionUniformName: String,
    var colorUniformName: String,
    var radiusUniformName: String,
    var position: Translator3f = Translator3f(),
    var color: Color3f = Color3f.white(),
    var radius: Vector3f = Vector3f(x = 1f)
) : Component {

    companion object {
        const val ID: Short = 5
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

}