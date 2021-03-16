package application.graphics.render

import application.core.ecs.System
import application.core.math.TransformComponent
import application.graphics.core.material.MaterialComponent
import application.graphics.core.mesh.MeshComponent
import application.graphics.core.scene.CameraComponent
import application.graphics.core.scene.ReflectionSceneComponent
import application.graphics.core.scene.RefractionSceneComponent
import application.graphics.math.ProjectionMatrix4f
import application.graphics.shader.ShaderComponent
import application.graphics.shader.uniforms.UVector4f

abstract class RenderSystem : System() {

    abstract val projection : ProjectionMatrix4f

    fun applyWindowAspectRatio(width: Float, height: Float) {
        projection.apply {
            screenWidth = width
            screenHeight = height
            apply()
        }
    }

    override fun onPrepareEntityGroup(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner

        shaderOwner.run {
            onCreate()
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
        super.onUpdateEntityGroup(entityGroupId)

        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner
        val indexCount = shaderOwner.meshBuffer.getIndexCount()
        val vertexCount = shaderOwner.meshBuffer.getVertexCount()

        shaderOwner.onStart()

        updateComponents(entityGroupId)

        if (indexCount == 0) {
            drawVertices(vertexCount)
        } else {
            drawIndices(indexCount)
        }

        shaderOwner.onStop()
    }

    protected open fun updateComponents(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner
        val meshComponent = entityGroup.getNonNullComponent<MeshComponent>(MeshComponent.ID)

        meshComponent.run {
            if (usesCullFace) {
                enableCullFace()
            } else {
                disableCullFace()
            }
        }

        shaderOwner.setUniform(projection.name, projection)

        val cameraComponent = entityGroup.getComponent<CameraComponent>(CameraComponent.ID)
        if (cameraComponent != null) {
            shaderOwner.setUniform(cameraComponent.transformMatrix4f.name, cameraComponent.transformMatrix4f)
        }
    }

    override fun onUpdateEntity(entityGroupId: Int, entityId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner
        val entityGroupMeshComponent = entityGroup.getNonNullComponent<MeshComponent>(MeshComponent.ID)
        val entityGroupMaterialComponent = entityGroup.getComponent<MaterialComponent>(MaterialComponent.ID)

        val entity = entityGroups[entityGroupId].entities[entityId]
        val entityTransformation = entity.getNonNullComponent<TransformComponent>(TransformComponent.ID)
        val entityMeshComponent = entity.getComponent<MeshComponent>(MeshComponent.ID)
        val entityMaterialComponent = entity.getComponent<MaterialComponent>(MaterialComponent.ID)

        if (entityMaterialComponent != null && entityMaterialComponent.isUpdated) {
            shaderOwner.materialBuffer.addMaterial(entityMaterialComponent.material)
        }

        if (entityTransformation.isUpdated) {

        }

        if (entityMeshComponent != null && entityMeshComponent.isUpdated) {
            shaderOwner.meshBuffer.addMesh(entityMeshComponent.mesh)
        }
    }

    protected abstract fun drawIndices(indexCount: Int)
    protected abstract fun drawVertices(vertexCount: Int)

    protected var polygonMode = 0

    fun enablePolygonMode(polygonMode: Int) {
        if (this.polygonMode != polygonMode) {
            this.polygonMode = polygonMode
            onPolygonMode()
        }
    }

    protected abstract fun onPolygonMode()

    private var isCullFaceEnabled = false

    private fun enableCullFace() {
        if (!isCullFaceEnabled) {
            isCullFaceEnabled = true
            onEnableCullFace()
        }
    }

    protected abstract fun onEnableCullFace()

    private fun disableCullFace() {
        if (isCullFaceEnabled) {
            isCullFaceEnabled = false
            onDisableCullFace()
        }
    }

    protected abstract fun onDisableCullFace()

    private var isClippingEnabled = false

    private fun enableClipping() {
        if (!isClippingEnabled) {
            isClippingEnabled = true
            onEnableClipping()
        }
    }

    protected abstract fun onEnableClipping()

    private fun disableClipping() {
        if (isClippingEnabled) {
            isClippingEnabled = false
            onDisableClipping()
        }
    }

    protected abstract fun onDisableClipping()

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