package application.graphics.render

import application.core.ecs.Entity
import application.core.ecs.EntityGroup
import application.core.ecs.System
import application.graphics.component.CameraComponent
import application.graphics.component.ColorComponent
import application.graphics.mesh.MeshComponent
import application.graphics.component.TextureComponent
import application.graphics.math.ProjectionMatrix4f
import application.graphics.shader.ShaderComponent

abstract class RenderSystem : System() {

    abstract val projection : ProjectionMatrix4f

    protected lateinit var shaderComponent: ShaderComponent

    fun applyWindowAspectRatio(width: Float, height: Float) {
        projection.screenWidth = width
        projection.screenHeight = height
        projection.apply()
        updateProjection()
    }

    private fun updateProjection() {
        shaderComponent.run {
            startShader()
            shaderOwner.setUniform(projection.name, projection)
            stopShader()
        }
    }

    override fun onCreate() {
        for (entityGroup in entityGroups) {
            val shaderComponent = entityGroup.getNonNullComponent<ShaderComponent>(ShaderComponent.ID)
            shaderComponent.shaderOwner.onCreate()
        }
    }

    override fun onPrepare(entityGroup: EntityGroup) {
        shaderComponent = entityGroup.getNonNullComponent(ShaderComponent.ID)

        val meshComponent = entityGroup.getComponent<MeshComponent>(MeshComponent.ID)
        meshComponent?.let {
            it.bind()
            for (vertexBuffer in it.vertexBuffers) {
                val vertex = vertexBuffer.vertex
                shaderComponent.shaderOwner.setVertex(vertexName = vertex.name, vertexAttribute = vertex.attribute)
            }
        }

        shaderComponent.shaderOwner.onPrepare()

        prepareProjection()
        updateProjection()

        prepareComponents(entityGroup)
        super.onPrepare(entityGroup)
    }

    protected open fun prepareComponents(entityGroup: EntityGroup) {
        val colorComponent = entityGroup.getComponent<ColorComponent>(ColorComponent.ID)
        colorComponent?.let {
            shaderComponent.shaderOwner.putUniformName(colorComponent.colorName)
        }

        val textureComponent = entityGroup.getComponent<TextureComponent>(TextureComponent.ID)
        if (textureComponent != null) {
            prepareTexture(textureComponent)
        }

        val cameraComponent = entityGroup.getComponent<CameraComponent>(CameraComponent.ID)
        cameraComponent?.let {
            shaderComponent.shaderOwner.putUniformName(it.transformMatrix4f.name)
        }
    }

    private fun prepareProjection() {
        shaderComponent.shaderOwner.putUniformName(projection.name)
    }

    private fun prepareTexture(textureComponent: TextureComponent) {
        for (texture in textureComponent.textures) {
            shaderComponent.shaderOwner.putUniformName(texture.uniformSamplerName)
            shaderComponent.shaderOwner.putUniformName(texture.textureGrid.gridOffsetName)
            shaderComponent.shaderOwner.putUniformName(texture.textureGrid.rowCountName)
        }
    }

    override fun onPrepare(entity: Entity) {
        shaderComponent.shaderOwner.putUniformName(entity.transformation.name)

        val textureComponent = entity.textureComponent
        if (textureComponent != null) {
            prepareTexture(textureComponent)
        }
    }

    override fun getRequiredComponentId(): Short = ShaderComponent.ID

    override fun onUpdate(entityGroup: EntityGroup) {
        shaderComponent = entityGroup.getNonNullComponent(ShaderComponent.ID)

        shaderComponent.startShader()

        updateComponents(entityGroup)

        val meshComponent = entityGroup.getComponent<MeshComponent>(MeshComponent.ID)
        meshComponent?.run {
            if (usesCullFace) {
                enableCullFace()
            } else {
                disableCullFace()
            }
            setPolygonMode(meshComponent.polygonMode)
            bindVertexArray()
            enableAttributes()
        }

        val drawFunction: ()-> Unit = if (meshComponent != null) {
            if (meshComponent.hasIndices()) {
                {
                    onDrawIndices(meshComponent.getIndexCount())
                }
            } else {
                {
                    onDrawVertices(meshComponent.getVertexCount())
                }
            }
        } else {
            {}
        }

        val textureComponent = entityGroup.getComponent<TextureComponent>(TextureComponent.ID)

        for (entity in entityGroup.entities) {
            onUpdate(entity)
            textureComponent?.let {
                updateTexture(it.apply {
                    textures[0].textureGrid.apply {
                        textureGridId = entity.textureGridId
                        apply()
                    }
                })
            }
            drawFunction.invoke()
        }

        meshComponent?.run {
            disableAttributes()
            unbindVertexArray()
        }

        shaderComponent.stopShader()
    }

    private fun updateTexture(textureComponent: TextureComponent) {
        for (texture in textureComponent.textures) {
            texture.run {
                shaderComponent.shaderOwner.setUniform(uniformName = texture.uniformSamplerName, uniformValue = texture.uniformSamplerValue)
                shaderComponent.shaderOwner.setUniform(uniformName = texture.textureGrid.gridOffsetName, uniformValue = texture.textureGrid.gridOffset)
                shaderComponent.shaderOwner.setUniform(uniformName = texture.textureGrid.rowCountName, uniformValue = texture.textureGrid.rowCountValue.toFloat())
                activateTexture()
                onBind()
            }
        }
    }

    protected open fun updateComponents(entityGroup: EntityGroup) {
        val colorComponent = entityGroup.getComponent<ColorComponent>(ColorComponent.ID)
        colorComponent?.let {
            shaderComponent.shaderOwner.setUniform(uniformName = colorComponent.colorName, uniformValue = colorComponent.color)
        }

        val cameraComponent = entityGroup.getComponent<CameraComponent>(CameraComponent.ID)
        cameraComponent?.let {
            shaderComponent.shaderOwner.setUniform(it.transformMatrix4f.name, it.transformMatrix4f)
        }
    }

    override fun onUpdate(entity: Entity) {
        val transform = entity.transformation
        shaderComponent.shaderOwner.setUniform(transform.name, transform)

        val textureComponent = entity.textureComponent
        if (textureComponent != null) {
            updateTexture(textureComponent)
        }
    }

    protected abstract fun onDrawIndices(indexCount: Int)
    protected abstract fun onDrawVertices(vertexCount: Int)

    override fun onDestroy() {
        super.onDestroy()
        shaderComponent.onDestroy()
    }

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

}