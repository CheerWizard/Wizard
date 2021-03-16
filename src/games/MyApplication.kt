package games

import application.core.ecs.Engine
import application.core.ecs.Entity
import application.core.ecs.EntityGroup
import application.core.math.TransformMatrix4f
import application.graphics.core.scene.LightComponent
import application.graphics.shader.Attribute2f
import application.graphics.shader.Attribute3f
import application.graphics.shader.Attribute4f
import application.graphics.core.buffers.MaterialBuffer
import application.graphics.core.material.MaterialComponent
import application.graphics.math.Color4f
import application.graphics.math.Rotator3f
import application.graphics.core.buffers.MeshBuffer
import application.graphics.core.mesh.MeshComponent
import application.graphics.render.Render3dSystem
import application.graphics.shader.ShaderComponent
import application.graphics.shader.uniforms.UColor3f
import application.graphics.shader.uniforms.UFloat
import application.graphics.shader.uniforms.UVector3f
import application.platform.GLEngine
import application.platform.GraphicsApplication
import application.platform.core.buffers.GLIndexBuffer
import application.platform.core.buffers.GLVertexArray
import application.platform.core.buffers.GLVertexBuffer
import application.platform.shader.GLFragmentShader
import application.platform.shader.GLShaderOwner
import application.platform.shader.GLVertexShader
import application.platform.texture.GLTexture2d
import org.joml.Random
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

class MyApplication : GraphicsApplication(title = "MyApplication") {

    override val engine: Engine = GLEngine(this)

    private var sunLightComponent = LightComponent(
        position = UVector3f(
            name = "lightPosition",
            x = 0f,
            y = 200f,
            z = 0f
        ),
        color = UColor3f(
            name = "lightColor",
            red = 1f,
            green = 1f,
            blue = 1f
        ),
        radius = UVector3f(
            name = "lightRadius",
            x = 0.75f,
            y = 0f,
            z = 0f
        )
    )

    override fun onCreate() {
        super.onCreate()

        objParser.parse("deagle.obj")

        val positions = objParser.getPositions()
        val indices = objParser.getIndices()
        val coordinates = objParser.getTextureCoordinates()
        val normals = objParser.getNormals()

        val meshCount = 1000

        val meshBuffer = MeshBuffer(
            indexBuffer = GLIndexBuffer(
                totalIndexCount = indices.size * meshCount
            ),
            positionBuffer = GLVertexBuffer(
                attribute = Attribute3f(
                    name = "position",
                    location = 0
                ),
                totalVertexCount = positions.size * meshCount
            ),
            normalsBuffer = GLVertexBuffer(
                attribute = Attribute3f(
                    name = "normal",
                    location = 1
                ),
                totalVertexCount = normals.size * meshCount
            )
        )

        val meshComponent = MeshComponent(
            vertexArray = GLVertexArray()
        ).apply {
            usesCullFace = true
            addMeshBuffer(meshBuffer)
        }

        val materialBuffer = MaterialBuffer(
            textureBuffer =  GLTexture2d(
                strengthUniformName = "diffuseSampler",
                detalization = 0f
            ).create("grey.png"),
            coordinatesBuffer = GLVertexBuffer(
                attribute = Attribute2f(
                    name = "coordinate",
                    location = 2
                ),
                totalVertexCount = coordinates.size * meshCount
            ),
            colorsBuffer = GLVertexBuffer(
                attribute = Attribute4f(
                    name = "color",
                    location = 3
                ),
                totalVertexCount = positions.size * meshCount
            )
        )

        val materialComponent = MaterialComponent().apply {
            addMaterialBuffer(materialBuffer)
        }

        val shaderComponent = ShaderComponent(
            shaderOwner = GLShaderOwner(
                vertexShader = GLVertexShader("cube_vertex").readFile(),
                fragmentShader = GLFragmentShader("cube_fragment").readFile()
            )
        )

        val specularComponent = SpecularComponent(
            shining = UFloat(
                name = "shining",
                value = 2f
            ),
            reflectivity = UFloat(
                name = "reflectivity",
                value = 1f
            ),
            brightness = UFloat(
                name = "brightness",
                value = 0.5f
            )
        )

        val skyGroup = EntityGroup().apply {
            putComponent(meshComponent)
            putComponent(materialComponent)
            putComponent(shaderComponent)
            putComponent(cameraComponent)
            putComponent(sunLightComponent)
            putComponent(specularComponent)
        }

        val random = Random(1000000)

        for (i in 0 until meshCount) {
            val r1 = random.nextFloat()
            val r2 = random.nextFloat()
            val r3 = random.nextFloat()

            val x = r1 * 100 - 50
            val y = r2 * 100 - 50
            val z = r3 * -300

            meshBuffer.addMesh(positions = positions, indices = indices, normals = normals)
            materialBuffer.fillColor(color = Color4f(red = random.nextFloat(), green = random.nextFloat(), blue = random.nextFloat()), vertexStart = positions.size * i, vertexEnd = positions.size * (i + 1) / 2)
            materialBuffer.fillColor(color = Color4f(red = random.nextFloat(), green = random.nextFloat(), blue = random.nextFloat()), vertexStart = positions.size * i / 2, vertexEnd = positions.size * (i + 1))
            materialBuffer.addCoordinates(coordinates)

            skyGroup.addEntity(
                Entity(
                    transformation = TransformMatrix4f(
                        position = Vector3f(x,y,z),
                        rotation = Rotator3f(x = x * 90f, y = x * 90f, z = z * 90f),
                    )
                )
            )
        }

        meshBuffer.readMode()
        materialBuffer.readMode()

        engine.fps = 144

        engine.addEntityGroup(systemId = Render3dSystem.ID, skyGroup)
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
        }
    }

    private fun moveLight(x: Float = 0f, y: Float = 0f, z: Float = 0f) {
        sunLightComponent.apply {
            position.x += x
            position.y += y
            position.z += z
        }
    }

}