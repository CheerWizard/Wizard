package application.graphics.render

import application.core.ecs.System
import application.graphics.component.CameraComponent
import application.graphics.component.MeshComponent
import application.graphics.material.MaterialComponent
import application.graphics.math.ProjectionMatrix4f
import application.graphics.scene.ReflectionSceneComponent
import application.graphics.scene.RefractionSceneComponent
import application.graphics.shader.ShaderComponent
import application.graphics.uniform.UVector4f

abstract class RenderSystem : System() {

    abstract val projection : ProjectionMatrix4f

    fun applyWindowAspectRatio(width: Float, height: Float) {
        projection.screenWidth = width
        projection.screenHeight = height
        projection.apply()
    }

    override fun onPrepareEntityGroup(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner
        val meshComponent = entityGroup.getNonNullComponent<MeshComponent>(MeshComponent.ID)

        shaderOwner.onCreate()

        meshComponent.prepare()
        for (attribute in meshComponent.vertexBuffer.getAttributes()) {
            shaderOwner.setAttribute(attributeLocation = attribute.location, attributeName = attribute.name)
        }

        shaderOwner.run {
            onPrepare()
            putUniformName(projection.name)
        }

        prepareComponents(entityGroupId)

        super.onPrepareEntityGroup(entityGroupId)
    }

    protected open fun prepareComponents(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner

        prepareSceneComponents(entityGroupId)

        val cameraComponent = entityGroup.getComponent<CameraComponent>(CameraComponent.ID)
        if (cameraComponent != null) {
            shaderOwner.putUniformName(cameraComponent.transformMatrix4f.name)
        }

        val materialComponent = entityGroup.getComponent<MaterialComponent>(MaterialComponent.ID)
        if (materialComponent != null) {
            prepareMaterial(entityGroupId = entityGroupId, materialComponent = materialComponent)
        }
    }

    private fun prepareSceneComponents(entityGroupId: Int) {
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

    private fun prepareMaterial(entityGroupId: Int, materialComponent: MaterialComponent) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner

        val materialColor = materialComponent.color
        if (materialColor != null) {
            shaderOwner.putUniformName(materialColor.name)
        }

        val materialParallax = materialComponent.parallax
        if (materialParallax != null) {
            shaderOwner.putUniformName(materialParallax.offsetUniformName)
        }

        for (texture in materialComponent.textures) {
            shaderOwner.run {
                putUniformName(texture.slotUniformName)
                putUniformName(texture.textureGrid.gridOffsetName)
                putUniformName(texture.textureGrid.rowCountName)
                putUniformName(texture.strengthUniformName)
            }
        }
    }

    override fun onPrepareEntity(entityGroupId: Int, entityId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val entity = entityGroup.entities[entityId]

        val materialComponent = entity.materialComponent
        if (materialComponent != null) {
            prepareMaterial(entityGroupId = entityGroupId, materialComponent = materialComponent)
        }
    }

    override fun getRequiredComponentId(): Short = ShaderComponent.ID

    override fun onUpdateEntityGroup(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderComponent = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID)
        val meshComponent = entityGroup.getComponent<MeshComponent>(MeshComponent.ID) ?: return

        shaderComponent.startShader()

        updateComponents(entityGroupId)

        meshComponent.run {
            if (usesCullFace) {
                enableCullFace()
            } else {
                disableCullFace()
            }

            setPolygonMode(meshComponent.getPolygonMode())

            bindVertexArray()
            enableAttributes()
            bindIndexBuffer()
            bindVertexBuffer()

            super.onUpdateEntityGroup(entityGroupId)

            onDrawIndices(indexBuffer.position())

            disableAttributes()
            unbindVertexArray()
        }

        shaderComponent.stopShader()
    }

    protected open fun updateComponents(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner

        shaderOwner.setUniform(projection.name, projection)

        val cameraComponent = entityGroup.getComponent<CameraComponent>(CameraComponent.ID)
        if (cameraComponent != null) {
            shaderOwner.setUniform(cameraComponent.transformMatrix4f.name, cameraComponent.transformMatrix4f)
        }

        val materialComponent = entityGroup.getComponent<MaterialComponent>(MaterialComponent.ID)
        if (materialComponent != null) {
            updateEntityGroupMaterial(entityGroupId)
        }
    }

    private fun updateEntityGroupMaterial(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderComponent = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID)
        val materialComponent = entityGroup.getNonNullComponent<MaterialComponent>(MaterialComponent.ID)

        val materialColor = materialComponent.color
        if (materialColor != null) {
            shaderComponent.shaderOwner.setUniform(materialColor.name, materialColor)
        }

        val materialParallax = materialComponent.parallax
        if (materialParallax != null) {
            shaderComponent.shaderOwner.setUniform(materialParallax.offsetUniformName, materialParallax.offset)
        }

        for (texture in materialComponent.textures) {
            shaderComponent.shaderOwner.run {
                setUniform(uniformName = texture.slotUniformName, uniformValue = texture.slot)
                setUniform(uniformName = texture.textureGrid.gridOffsetName, uniformValue = texture.textureGrid.gridOffset)
                setUniform(uniformName = texture.textureGrid.rowCountName, uniformValue = texture.textureGrid.rowCountValue.toFloat())
                setUniform(uniformName = texture.strengthUniformName, uniformValue = texture.strength)
            }
            texture.activateTexture()
            texture.bind()
        }
    }

    override fun onUpdateEntity(entityGroupId: Int, entityId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val entity = entityGroup.entities[entityId]
        val meshComponent = entityGroup.getNonNullComponent<MeshComponent>(MeshComponent.ID)

        meshComponent.vertexBuffer.transformMesh(meshId = entityId, matrix4f = entity.transformation)

        val materialComponent = entity.materialComponent
        if (materialComponent != null) {
            updateEntityMaterial(entityGroupId = entityGroupId, entityId = entityId)
        }
    }

    private fun updateEntityMaterial(entityGroupId: Int, entityId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val entity = entityGroup.entities[entityId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner
        val materialComponent = entity.materialComponent as MaterialComponent

        val materialColor = materialComponent.color
        if (materialColor != null) {
            shaderOwner.setUniform(materialColor.name, materialColor)
        }

        val materialParallax = materialComponent.parallax
        if (materialParallax != null) {
            shaderOwner.setUniform(materialParallax.offsetUniformName, materialParallax.offset)
        }

        for (texture in materialComponent.textures) {
            shaderOwner.run {
                setUniform(uniformName = texture.slotUniformName, uniformValue = texture.slot)
                setUniform(uniformName = texture.textureGrid.gridOffsetName, uniformValue = texture.textureGrid.gridOffset)
                setUniform(uniformName = texture.textureGrid.rowCountName, uniformValue = texture.textureGrid.rowCountValue.toFloat())
                setUniform(uniformName = texture.strengthUniformName, uniformValue = texture.strength)
            }
            texture.activateTexture()
            texture.bind()
        }
    }

    protected abstract fun onDrawIndices(indexCount: Int)
    protected abstract fun onDrawVertices(vertexCount: Int)

    private var polygonMode = 0

    protected open fun setPolygonMode(polygonMode: Int) {
        if (this.polygonMode == polygonMode) return
        this.polygonMode = polygonMode
    }

    private var isCullFaceEnabled = false

    protected open fun enableCullFace() {
        if (isCullFaceEnabled) return
        isCullFaceEnabled = true
    }

    protected open fun disableCullFace() {
        if (!isCullFaceEnabled) return
        isCullFaceEnabled = false
    }

    private var isClippingEnabled = false

    protected open fun enableClipping() {
        if (isClippingEnabled) return
        isClippingEnabled = true
    }

    protected open fun disableClipping() {
        if (!isClippingEnabled) return
        isClippingEnabled = false
    }

    override fun onUpdate() {
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

        super.onUpdate()
    }

    private fun updateClipping(clipping: UVector4f?) {
        if (clipping != null) {
            enableClipping()
            for (entityGroupId in entityGroups.indices) {
                val shaderComponent = entityGroups[entityGroupId].getNonNullComponent<ShaderComponent>(ShaderComponent.ID)
                shaderComponent.shaderOwner.setUniform(clipping.name, clipping)
                onUpdateEntityGroup(entityGroupId)
            }
        } else {
            super.onUpdate()
        }
    }

}