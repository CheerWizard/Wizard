package application.graphics.render

import application.core.ecs.System
import application.graphics.component.CameraComponent
import application.graphics.mesh.MeshComponent
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
        val materialComponent = entityGroup.getComponent<MaterialComponent>(MaterialComponent.ID)

        shaderOwner.onCreate()

        meshComponent.run {
            bindVertexArray()
            prepareMeshBuffers()
            materialComponent?.prepareMaterialBuffers()
            unbindVertexArray()
        }

        for (meshBuffer in meshComponent.meshBuffers) {
            val positionAttribute = meshBuffer.positionBuffer.attribute
            shaderOwner.setAttribute(attributeLocation = positionAttribute.location, attributeName = positionAttribute.name)
            val normalsBuffer = meshBuffer.normalsBuffer
            if (normalsBuffer != null) {
                shaderOwner.setAttribute(attributeLocation = normalsBuffer.attribute.location, attributeName = normalsBuffer.attribute.name)
            }
        }

        if (materialComponent != null) {
            for (materialBuffer in materialComponent.materialBuffers) {
                val coordinatesAttribute = materialBuffer.coordinatesBuffer.attribute
                shaderOwner.setAttribute(attributeLocation = coordinatesAttribute.location, attributeName = coordinatesAttribute.name)
                val colorsBuffer = materialBuffer.colorsBuffer
                if (colorsBuffer != null) {
                    shaderOwner.setAttribute(attributeLocation = colorsBuffer.attribute.location, attributeName = colorsBuffer.attribute.name)
                }
            }
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

    override fun onPrepareEntity(entityGroupId: Int, entityId: Int) {
    }

    override fun getRequiredComponentId(): Short = ShaderComponent.ID

    override fun onUpdateEntityGroup(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderComponent = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID)
        val meshComponent = entityGroup.getNonNullComponent<MeshComponent>(MeshComponent.ID)
        val materialComponent = entityGroup.getComponent<MaterialComponent>(MaterialComponent.ID)

        shaderComponent.startShader()

        updateComponents(entityGroupId)

        meshComponent.run {
            if (usesCullFace) {
                enableCullFace()
            } else {
                disableCullFace()
            }
//            setPolygonMode(meshGroupComponent.getPolygonMode())
        }

        meshComponent.bindVertexArray()

        if (materialComponent != null) {
            updateAllBuffers(entityGroupId)
        } else {
            updateMeshBuffer(entityGroupId)
        }

        meshComponent.unbindVertexArray()

        shaderComponent.stopShader()
    }

    private fun updateMeshBuffer(entityGroupId: Int) {
        val meshBuffer = entityGroups[entityGroupId].getNonNullComponent<MeshComponent>(MeshComponent.ID).meshBuffers[0]

        super.onUpdateEntityGroup(entityGroupId)

        meshBuffer.enableAttributes()

        meshBuffer.bind()

        draw(meshBuffer.getIndexCount())

        meshBuffer.disableAttributes()
    }

    private fun updateAllBuffers(entityGroupId: Int) {
        val meshBuffer = entityGroups[entityGroupId].getNonNullComponent<MeshComponent>(MeshComponent.ID).meshBuffers[0]
        val materialBuffer = entityGroups[entityGroupId].getNonNullComponent<MaterialComponent>(MaterialComponent.ID).materialBuffers[0]

        super.onUpdateEntityGroup(entityGroupId)

        meshBuffer.enableAttributes()
        materialBuffer.enableAttributes()

        meshBuffer.bind()
        materialBuffer.bind()

        draw(meshBuffer.getIndexCount())

        materialBuffer.disableAttributes()
        meshBuffer.disableAttributes()
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
        val materialComponent = entityGroup.getNonNullComponent<MaterialComponent>(MaterialComponent.ID)

        for (materialBuffer in materialComponent.materialBuffers) {
            materialBuffer.textureBuffer.run {
                activateTexture()
                bind()
            }
        }
    }

    override fun onUpdateEntity(entityGroupId: Int, entityId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val entity = entityGroup.entities[entityId]
        val meshGroupComponent = entityGroup.getNonNullComponent<MeshComponent>(MeshComponent.ID)

        meshGroupComponent.getMeshBuffer(0).apply {
            transformMesh(meshId = entityId, matrix4f = entity.transformation)
        }
    }

    protected abstract fun draw(indexCount: Int)
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