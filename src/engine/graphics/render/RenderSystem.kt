package engine.graphics.render

import engine.core.ecs.System
import engine.graphics.core.mesh.MeshComponent
import engine.graphics.math.Projection3dMatrix4f
import engine.graphics.shader.ShaderComponent
import engine.graphics.shader.uniforms.UVector4f

abstract class RenderSystem(screenWidth: Float, screenHeight: Float) : System() {

    protected val projection = Projection3dMatrix4f(name = "projection", screenWidth = screenWidth, screenHeight = screenHeight)

    fun applyWindowAspectRatio(width: Float, height: Float) {
        projection.apply {
            screenWidth = width
            screenHeight = height
            applyChanges()
        }
    }

    override fun onPrepareEntityGroup(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner

        val meshBuffer = shaderOwner.meshBuffer

        meshBuffer.allocateBuffer()
        meshBuffer.writeBuffers()

        val meshComponent = entityGroup.getComponent<MeshComponent>(MeshComponent.ID)

        if (meshComponent != null) {
            meshBuffer.addMesh(meshComponent.mesh)
        }

        super.onPrepareEntityGroup(entityGroupId)

        meshBuffer.readBuffers()

        shaderOwner.run {
            onCreate()
            onPrepare()
            putUniformName(projection.name)
            prepareSamplers()
        }

        prepareComponents(entityGroupId)
        prepareSceneComponents(entityGroupId)
    }

    protected abstract fun prepareComponents(entityGroupId: Int)

    protected abstract fun prepareSceneComponents(entityGroupId: Int)

    override fun onPrepareEntity(entityGroupId: Int, entityId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner

        val meshBuffer = shaderOwner.meshBuffer

        val entity = entityGroup.entities[entityId]
        val meshComponent = entity.getComponent<MeshComponent>(MeshComponent.ID)

        if (meshComponent != null) {
            meshBuffer.addMesh(meshComponent.mesh)
        }
    }

    override fun getRequiredComponentId(): Short = ShaderComponent.ID

    override fun onUpdateEntityGroup(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]

        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner
        val meshBuffer = shaderOwner.meshBuffer

        val meshComponent = entityGroup.getComponent<MeshComponent>(MeshComponent.ID)

        meshBuffer.writeSubBuffers()

        if (meshComponent != null) {
            meshBuffer.tryUpdateMesh(meshComponent.mesh)
        }

        super.onUpdateEntityGroup(entityGroupId)

        shaderOwner.onStart()

        updateComponents(entityGroupId)

        val vertexCount = meshBuffer.getVertexCount()
        val indexCount = meshBuffer.getIndexCount()
        val instanceCount = entityGroup.entities.size

        if (indexCount == 0) {
            if (instanceCount > 1) {
                drawArrays(vertexCount = vertexCount, instanceCount = instanceCount)
            } else {
                drawArrays(vertexCount = vertexCount)
            }
        } else {
            if (instanceCount > 1) {
                drawElements(indexCount = indexCount, instanceCount = instanceCount)
            } else {
                drawElements(indexCount = indexCount)
            }
        }

        shaderOwner.onStop()
    }

    protected open fun updateComponents(entityGroupId: Int) {
        val entityGroup = entityGroups[entityGroupId]
        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner
        shaderOwner.updateUniform(projection)
        shaderOwner.updateSamplers()
    }

    override fun onUpdateEntity(entityGroupId: Int, entityId: Int) {
        val entityGroup = entityGroups[entityGroupId]

        val entity = entityGroup.entities[entityId]
        val entityMeshComponent = entity.getComponent<MeshComponent>(MeshComponent.ID)

        val shaderOwner = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID).shaderOwner
        val meshBuffer = shaderOwner.meshBuffer

        if (entityMeshComponent != null) {
            meshBuffer.tryUpdateMesh(entityMeshComponent.mesh)
        }
    }

    protected abstract fun drawElements(indexCount: Int)
    protected abstract fun drawElements(indexCount: Int, instanceCount: Int)

    protected abstract fun drawArrays(vertexCount: Int)
    protected abstract fun drawArrays(vertexCount: Int, instanceCount: Int)

    protected var polygonMode = 0

    fun enablePolygonMode(polygonMode: Int) {
        if (this.polygonMode != polygonMode) {
            this.polygonMode = polygonMode
            onPolygonMode()
        }
    }

    protected abstract fun onPolygonMode()

    protected var isCullFaceEnabled = false

    protected fun enableCullFace() {
        if (!isCullFaceEnabled) {
            isCullFaceEnabled = true
            onEnableCullFace()
        }
    }

    protected abstract fun onEnableCullFace()

    protected fun disableCullFace() {
        if (isCullFaceEnabled) {
            isCullFaceEnabled = false
            onDisableCullFace()
        }
    }

    protected abstract fun onDisableCullFace()

    private var isClippingEnabled = false

    protected fun enableClipping() {
        if (!isClippingEnabled) {
            isClippingEnabled = true
            onEnableClipping()
        }
    }

    protected abstract fun onEnableClipping()

    protected fun disableClipping() {
        if (isClippingEnabled) {
            isClippingEnabled = false
            onDisableClipping()
        }
    }

    protected abstract fun onDisableClipping()

    protected fun updateClipping(clipping: UVector4f?) {
        if (clipping != null) {
            enableClipping()
            for (entityGroupId in entityGroups.indices) {
                val shaderComponent = entityGroups[entityGroupId].getNonNullComponent<ShaderComponent>(ShaderComponent.ID)
                shaderComponent.shaderOwner.updateUniform(clipping)
                onUpdateEntityGroup(entityGroupId)
            }
        } else {
            super.onUpdate()
        }
    }

}