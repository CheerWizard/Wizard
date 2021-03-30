package engine.graphics.render

import engine.graphics.core.mesh.MeshComponent
import engine.graphics.core.scene.CameraComponent
import engine.graphics.core.scene.LightComponent
import engine.graphics.core.scene.ReflectionSceneComponent
import engine.graphics.core.scene.RefractionSceneComponent
import engine.graphics.shader.ShaderComponent

abstract class Render3dSystem (screenWidth: Float, screenHeight: Float) : RenderSystem(screenWidth = screenWidth, screenHeight = screenHeight) {

    override val tag: String = Render3dSystem::class.java.simpleName
    override val priority: Byte = 0

    override fun prepareComponents(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner

        val cameraComponent = entityGroup.getComponent<CameraComponent>(CameraComponent.ID)
        if (cameraComponent != null) {
            shaderOwner.putUniformName(cameraComponent.transformation.name)
        }

        val lightComponent = entityGroup.getComponent<LightComponent>(LightComponent.ID)
        if (lightComponent != null) {
            shaderOwner.run {
                putUniformName(lightComponent.position.name)
                putUniformName(lightComponent.color.name)
                putUniformName(lightComponent.radius.name)
            }
        }
    }

    override fun prepareSceneComponents(entityGroupId: Int) {
        val shaderOwner = entityGroups[entityGroupId].getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner
        val reflectionComponent = getSceneComponent<ReflectionSceneComponent>(ReflectionSceneComponent.ID)
        val refractionComponent = getSceneComponent<RefractionSceneComponent>(RefractionSceneComponent.ID)

        if (reflectionComponent != null) {
            val clipping = reflectionComponent.clipping
            if (clipping != null) {
                shaderOwner.putUniformName(clipping.name)
            }
        }

        if (refractionComponent != null) {
            val clipping = refractionComponent.clipping
            if (clipping != null) {
                shaderOwner.putUniformName(clipping.name)
            }
        }
    }

    override fun updateSceneComponents() {
        isCullFaceEnabled = false
        onDisableCullFace()

        val reflectionComponent = getSceneComponent<ReflectionSceneComponent>(ReflectionSceneComponent.ID)
        if (reflectionComponent != null) {
            reflectionComponent.bind()
            updateClipping(reflectionComponent.clipping)
            reflectionComponent.unbind()
        }

        val refractionComponent = getSceneComponent<RefractionSceneComponent>(RefractionSceneComponent.ID)
        if (refractionComponent != null) {
            refractionComponent.bind()
            updateClipping(refractionComponent.clipping)
            refractionComponent.unbind()
        }

        disableClipping()
    }

    override fun updateComponents(entityGroupId: Int) {
        super.updateComponents(entityGroupId)

        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner
        val meshComponent = entityGroup.getComponent<MeshComponent>(MeshComponent.ID)

        if (meshComponent != null && meshComponent.cullFaceEnabled) {
            enableCullFace()
        } else {
            disableCullFace()
        }

        val cameraComponent = entityGroup.getComponent<CameraComponent>(CameraComponent.ID)
        if (cameraComponent != null) {
            shaderOwner.updateUniform(cameraComponent.transformation)
        }

        val lightComponent = entityGroup.getComponent<LightComponent>(LightComponent.ID)
        if (lightComponent != null) {
            shaderOwner.run {
                updateUniform(lightComponent.position)
                updateUniform(lightComponent.color)
                updateUniform(lightComponent.radius)
            }
        }
    }

}