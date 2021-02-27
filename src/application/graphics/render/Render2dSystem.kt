package application.graphics.render

import application.graphics.math.Projection2dMatrix4f
import application.graphics.math.ProjectionMatrix4f

abstract class Render2dSystem(screenWidth: Float, screenHeight: Float) : RenderSystem() {

    companion object {
        const val ID : Byte = 2
    }

    override fun getId(): Byte = ID

    override val projection: ProjectionMatrix4f = Projection2dMatrix4f(
        name = "projection2d",
        screenHeight = screenHeight,
        screenWidth = screenWidth
    )

}