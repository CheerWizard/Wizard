package engine.graphics.core.scene

import engine.core.ecs.Component
import engine.graphics.shader.uniforms.UColor3f
import engine.graphics.shader.uniforms.UVector3f

class LightComponent(
    var position: UVector3f = UVector3f(),
    var color: UColor3f = UColor3f(),
    var radius: UVector3f = UVector3f(x = 1f)
) : Component {

    companion object {
        const val ID: Short = 6
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

}