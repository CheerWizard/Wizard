package games

import application.core.ecs.Engine
import application.core.ecs.Entity
import application.core.ecs.EntityGroup
import application.core.math.TransformMatrix4f
import application.core.math.Vector3f
import application.graphics.component.LightComponent
import application.graphics.component.MeshComponent
import application.graphics.component.SpecularComponent
import application.graphics.material.MaterialComponent
import application.graphics.material.Parallax
import application.graphics.math.Translator3f
import application.graphics.render.Render3dSystem
import application.graphics.shader.ShaderComponent
import application.graphics.texture.TextureGrid
import application.platform.GLApplication
import application.platform.GLEngine
import application.platform.shader.GLFragmentShader
import application.platform.shader.GLShaderOwner
import application.platform.shader.GLVertexShader
import application.platform.texture.GLTexture2d
import application.platform.vertex.GLIndexBuffer
import application.platform.vertex.GLVertexArray
import application.platform.vertex.GLVertexBuffer
import org.lwjgl.glfw.GLFW

class MyApplication : GLApplication(title = "MyApplication") {

    override val engine: Engine = GLEngine(this)

    private var sunLightComponent = LightComponent(
        positionUniformName = "lightPosition",
        colorUniformName = "lightColor",
        radiusUniformName = "lightRadius",
        radius = Vector3f(
            x = 0.75f
        ),
        position = Translator3f(y = 200f)
    )

    override fun onCreate() {
        super.onCreate()

        val cubeShaderComponent = ShaderComponent(
            shaderOwner = GLShaderOwner(
                vertexShader = GLVertexShader("normal_mapping_vertex.glsl").readFile(),
                fragmentShader = GLFragmentShader("normal_mapping_fragment.glsl").readFile()
            )
        )

        val textureGrid = TextureGrid(
            gridOffsetName = "textureGridOffset",
            rowCountName = "textureGridRowCount",
        )

        val waterDiffuse = GLTexture2d(
            samplerUniformName = "diffuseSampler1",
            textureGrid = textureGrid,
            detalization = 5f,
            strengthUniformName = "diffuseStrength1"
        ).create("water_diffuse.jpg")

        val waterNormal = GLTexture2d(
            samplerUniformName = "normalSampler",
            textureGrid = textureGrid,
            strengthUniformName = "normalStrength"
        ).create("water_normal.jpg")

        val rockSpecular = GLTexture2d(
            samplerUniformName = "specularSampler",
            textureGrid = textureGrid,
            strengthUniformName = "specularStrength"
        ).create("rock_specular.png")

        val waterParallax = GLTexture2d(
            samplerUniformName = "parallaxSampler",
            textureGrid = textureGrid,
            strengthUniformName = "parallaxStrength",
            strength = 0.35f
        ).create("water_parallax.png")

        val grass = GLTexture2d(
            samplerUniformName = "diffuseSampler2",
            textureGrid = textureGrid,
            detalization = 5f,
            strengthUniformName = "diffuseStrength2",
            strength = 0.25f
        ).create("grass2.jpg")

        val parallax = Parallax(
            offsetUniformName = "parallaxOffset",
            offset = 0f,
        )

        val cubeTextureComponent = MaterialComponent().apply {
            addTexture(waterDiffuse)
            addTexture(waterNormal)
            addTexture(waterParallax)
            addTexture(rockSpecular)
            this.parallax = parallax
        }

        val specularComponent = SpecularComponent(
            reflectivityUniformName = "reflectivity",
            shiningUniformName = "shining",
            shining = 10f,
            brightnessUniformName = "brightness",
            brightness = 0.75f,
            reflectivity = 4f
        )

        terrainParser.apply {
            gridCount = 10
            parseHeightMap("rock_height.png")
        }

        val vertexBuffer = terrainParser.getVertexBuffer("cubeVertices")
        val textureBuffer = terrainParser.getTextureBuffer("cubeTextures")
        val normalBuffer = terrainParser.getNormalBuffer("cubeNormals")
        val indices = terrainParser.getIndices()
        val tangentBuffer = terrainParser.getVertexBuffer("cubeTangents")

        val cubeMeshComponent = MeshComponent(
            vertexArray = GLVertexArray(),
            vertexBuffer = GLVertexBuffer(),
            indexBuffer = GLIndexBuffer()
        )

        val plate = EntityGroup().apply {
            putComponent(cameraComponent)
            putComponent(cubeShaderComponent)
            putComponent(cubeMeshComponent)
            putComponent(cubeTextureComponent)
            putComponent(sunLightComponent)
            putComponent(specularComponent)
            addEntity(Entity(
                transformation = TransformMatrix4f(
                    name = "cubeTransform",
                    scalar = Vector3f(x = 10f, y = 1f, z = 10f)
                )
            ))
        }

        plateId = engine.addEntityGroup(systemId = Render3dSystem.ID, entityGroup = plate)
    }

    private var plateId = 0

    override fun bindInputController() {
        super.bindInputController()
        inputController.run {
            bindKeyHoldEvent(GLFW.GLFW_KEY_UP) { moveLight(x = 0f, y = 0f, z = -0.5f) }
            bindKeyHoldEvent(GLFW.GLFW_KEY_DOWN) { moveLight(x = 0f, y = 0f, z = 0.5f) }
            bindKeyHoldEvent(GLFW.GLFW_KEY_RIGHT) { moveLight(x = -0.5f, y = 0f, z = 0f) }
            bindKeyHoldEvent(GLFW.GLFW_KEY_LEFT) { moveLight(x = 0.5f, y = 0f, z = 0f) }
            bindKeyPressedEvent(GLFW.GLFW_KEY_L) { setPolygonMode(GLVertexBuffer.LINE_MODE) }
            bindKeyPressedEvent(GLFW.GLFW_KEY_P) { setPolygonMode(GLVertexBuffer.POINT_MODE) }
            bindKeyPressedEvent(GLFW.GLFW_KEY_F) { setPolygonMode(GLVertexBuffer.FILL_MODE) }
        }
    }

    private fun setPolygonMode(polygonMode: Int) {
        engine.getComponent<Render3dSystem, MeshComponent>(
            systemId = Render3dSystem.ID,
            entityGroupId = plateId,
            componentId = MeshComponent.ID
        )?.apply {
            vertexBuffer.polygonMode = polygonMode
        }
    }

    private fun moveLight(x: Float = 0f, y: Float = 0f, z: Float = 0f) {
        sunLightComponent.apply {
            position.addX(x)
            position.addY(y)
            position.addZ(z)
        }
    }

}