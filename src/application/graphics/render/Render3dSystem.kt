package application.graphics.render

import application.core.ecs.EntityGroup
import application.graphics.component.LightComponent
import application.graphics.component.SpecularComponent
import application.graphics.math.Projection3dMatrix4f
import application.graphics.math.ProjectionMatrix4f

abstract class Render3dSystem(screenWidth: Float, screenHeight: Float) : RenderSystem() {

    companion object {
        const val ID : Byte = 1
    }

    override fun getId(): Byte = ID

    override val projection: ProjectionMatrix4f = Projection3dMatrix4f(
        name = "projection3d",
        screenWidth = screenWidth,
        screenHeight = screenHeight
    )

    override fun prepareComponents(entityGroup: EntityGroup) {
        super.prepareComponents(entityGroup)

        val lightComponent = entityGroup.getComponent<LightComponent>(LightComponent.ID)
        lightComponent?.let {
            shaderComponent.shaderOwner.run {
                putUniformName(it.positionUniformName)
                putUniformName(it.colorUniformName)
                putUniformName(it.radiusUniformName)
            }
        }

        val specularComponent = entityGroup.getComponent<SpecularComponent>(SpecularComponent.ID)
        specularComponent?.let {
            shaderComponent.shaderOwner.run {
                putUniformName(it.reflectivityUniformName)
                putUniformName(it.shiningUniformName)
                putUniformName(it.brightnessUniformName)
            }
        }
    }

    override fun updateComponents(entityGroup: EntityGroup) {
        super.updateComponents(entityGroup)

        val lightComponent = entityGroup.getComponent<LightComponent>(LightComponent.ID)
        lightComponent?.let {
            shaderComponent.shaderOwner.run {
                setUniform(it.positionUniformName, it.position)
                setUniform(it.colorUniformName, it.color)
                setUniform(it.radiusUniformName, it.radius)
            }
        }

        val specularComponent = entityGroup.getComponent<SpecularComponent>(SpecularComponent.ID)
        specularComponent?.let {
            shaderComponent.shaderOwner.run {
                setUniform(it.reflectivityUniformName, it.reflectivity)
                setUniform(it.shiningUniformName, it.shining)
                setUniform(it.brightnessUniformName, it.brightness)
            }
        }
    }

}