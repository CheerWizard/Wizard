package games

import application.core.ecs.Engine
import application.core.ecs.Entity
import application.core.ecs.EntityGroup
import application.core.math.TransformMatrix4f
import application.core.math.Vector3f
import application.graphics.render.Render3dSystem
import application.graphics.component.ColorComponent
import application.graphics.component.LightComponent
import application.graphics.component.SpecularComponent
import application.graphics.component.TextureComponent
import application.graphics.math.Color4f
import application.graphics.math.Rotator3f
import application.graphics.math.Translator3f
import application.graphics.shader.ShaderComponent
import application.graphics.texture.TextureGrid
import application.platform.GLApplication
import application.platform.GLEngine
import application.platform.mesh.GLMeshComponent
import application.platform.hud.GLTextParser
import application.platform.shader.GLFragmentShader
import application.platform.shader.GLShaderOwner
import application.platform.shader.GLVertexShader
import application.platform.texture.GLTexture2d
import org.lwjgl.glfw.GLFW
import java.util.*
import kotlin.collections.ArrayList

class MyApplication : GLApplication(title = "MyApplication") {

    override val engine: Engine = GLEngine(this)

    private val textParser = GLTextParser()

    private var sunLightComponent = LightComponent(
        positionUniformName = "sunLightPosition",
        colorUniformName = "sunLightColor",
        radiusUniformName = "sunLightRadius",
        radius = Vector3f(
            x = 2f
        )
    )

    override fun onCreate() {
        super.onCreate()

        val cubeShaderComponent = ShaderComponent(
            shaderOwner = GLShaderOwner(
                vertexShader = GLVertexShader("cube_vertex.glsl").readFile(),
                fragmentShader = GLFragmentShader("cube_fragment.glsl").readFile()
            )
        )

        val normalMappingShaderComponent = ShaderComponent(
            shaderOwner = GLShaderOwner(
                vertexShader = GLVertexShader("normal_mapping_vertex.glsl").readFile(),
                fragmentShader = GLFragmentShader("normal_mapping_fragment.glsl").readFile()
            )
        )

        val crateTexture = GLTexture2d(
            uniformName = "textureSampler",
            textureGrid = TextureGrid(
                gridOffsetName = "textureGridOffset",
                rowCountName = "textureGridRowCount",
            ),
            detalization = 5f
        ).create("crate.png")

        val crateNormalTexture = GLTexture2d(
            uniformName = "normalsSampler",
            textureGrid = TextureGrid(
                gridOffsetName = "textureGridOffset",
                rowCountName = "textureGridRowCount"
            ),
            detalization = 5f
        ).create("crateNormal.png")

        val crateTextureComponent = TextureComponent().apply {
            addTexture(crateTexture)
        }

        val crateNormalTextureComponent = TextureComponent().apply {
            addTexture(crateTexture)
            addTexture(crateNormalTexture)
        }

        val specularComponent = SpecularComponent(
            reflectivityUniformName = "reflectivity",
            shiningUniformName = "shining",
            shining = 8f,
            brightnessUniformName = "brightness",
            brightness = 0.25f,
        )

        val crate = createCrate().apply {
            putComponent(cubeShaderComponent)
            putComponent(sunLightComponent)
            putComponent(crateTextureComponent)
            putComponent(specularComponent)
            addEntity(
                Entity(
                    transformation = TransformMatrix4f(
                        name = "cubeTransform",
                        scale = 0.1f
                    )
                )
            )
        }

        val crateNormal = createCrate().apply {
            putComponent(normalMappingShaderComponent)
            putComponent(sunLightComponent)
            putComponent(crateNormalTextureComponent)
            putComponent(specularComponent)
            addEntity(
                Entity(
                    transformation = TransformMatrix4f(
                        name = "cubeTransform",
                        scale = 0.1f,
                        position = Translator3f(
                            x = -30f
                        )
                    )
                )
            )
        }

        engine.addEntityGroup(systemId = Render3dSystem.ID, entityGroup = crate)
        engine.addEntityGroup(systemId = Render3dSystem.ID, entityGroup = crateNormal)
    }

    private fun createCrate(): EntityGroup {
        objParser.parse("crate.obj")

        val vertexBuffer = objParser.createVertexBuffer(name = "cubeVertices")
        val textureBuffer = objParser.createTextureBuffer(name = "cubeTextures")
        val normalsBuffer = objParser.createNormalBuffer(name = "cubeNormals")
        val tangentsBuffer = objParser.createVertexBuffer(name = "cubeTangents")
        val indices = objParser.getIndices()

        val meshComponent = GLMeshComponent(indices = indices).apply {
            addVertexBuffer(vertexBuffer)
            addVertexBuffer(textureBuffer)
            addVertexBuffer(normalsBuffer)
            addVertexBuffer(tangentsBuffer)
            usesCullFace = false
        }

        return EntityGroup().apply {
            putComponent(cameraComponent)
            putComponent(meshComponent)
        }
    }

    private fun createHud() {
//        val text = createText(
//            text = "Dragon",
//            fontFileName = "font.png",
//            textColor = Color4f()
//        ).apply {
//            addEntity(
//                Entity(
//                    transformation = TransformMatrix4f(
//                        name = "hudTransform"
//                    )
//                )
//            )
//        }
//
//        val square = createGroup(
//            objFileName = "rooster.obj",
//            textureFileName = "wood.jpg"
//        ).apply {
//            addEntity(
//                Entity(
//                    transformation = TransformMatrix4f(
//                        name = "hudTransform"
//                    )
//                )
//            )
//        }

//        engine.addEntityGroup(systemId = HudSystem.ID, text)
//        engine.addEntityGroup(systemId = HudSystem.ID, square)
    }

    private fun createText(
        fontFileName: String,
        text: String,
        textColor: Color4f
    ) : EntityGroup {

        textParser.createTexture().create(fontFileName)
        textParser.parse(text)

        val vertexBuffer = textParser.createVertexBuffer("hudVertices")
        val textureBuffer = textParser.createTextureBuffer("hudTextures")
        val indices = textParser.getIndices()

        val vertexComponent = GLMeshComponent(indices = indices).apply {
            addVertexBuffer(vertexBuffer)
            addVertexBuffer(textureBuffer)
        }

        val colorComponent = ColorComponent(
            colorName = "hudColor",
            color = textColor
        )

        return EntityGroup().apply {
            putComponent(vertexComponent)
            putComponent(colorComponent)
        }
    }

    private fun createEntities(count: Int): ArrayList<Entity> {
        val random = Random()
        val entities = ArrayList<Entity>()

        repeat(count) {
            val x = random.nextFloat() * 100 - 50
            val y = random.nextFloat() * 100 - 50
            val z = random.nextFloat() * -300
            val rx = random.nextFloat() * 180
            val ry = random.nextFloat() * 180
            val rz = random.nextFloat() * 180

            val transformation = TransformMatrix4f(
                name = "cubeTransform",
                position = Translator3f(
                    x = x,
                    y = y,
                    z = z
                ),
                rotation = Rotator3f().apply {
                    setXDegree(rx.toDouble())
                    setYDegree(ry.toDouble())
                    setZDegree(rz.toDouble())
                },
            )

            entities.add(Entity(transformation = transformation))
        }

        return entities
    }

    override fun bindInputController() {
        super.bindInputController()
        inputController.run {
            bindKeyHoldEvent(GLFW.GLFW_KEY_UP) { moveLight(x = 0f, y = 1f, z = 0f) }
            bindKeyHoldEvent(GLFW.GLFW_KEY_DOWN) { moveLight(x = 0f, y = -1f, z = 0f) }
            bindKeyHoldEvent(GLFW.GLFW_KEY_RIGHT) { moveLight(x = 1f, y = 0f, z = 0f) }
            bindKeyHoldEvent(GLFW.GLFW_KEY_LEFT) { moveLight(x = -1f, y = 0f, z = 0f) }
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