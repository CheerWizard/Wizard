package sandbox

import engine.core.Updatable
import engine.core.ecs.Engine
import engine.core.ecs.Entity
import engine.core.ecs.EntityGroup
import engine.core.math.TransformMatrix4f
import engine.graphics.core.mesh.Mesh
import engine.graphics.core.mesh.MeshComponent
import engine.graphics.geometry.Square
import engine.graphics.math.Color4f
import engine.graphics.render.Render2dSystem
import engine.graphics.render.Render3dSystem
import engine.graphics.shader.attributes.*
import engine.platform.GLEngine
import imgui.ImGuiApplication
import org.joml.Vector3f

class SandboxApplication : ImGuiApplication() {

    override val engine: Engine = GLEngine(title = "Sandbox", client = this)

//    override var cursorX: Float = engine.window.getCursorCenterX()
//    override var cursorY: Float = engine.window.getCursorCenterY()

    override fun onCreate() {
        super.onCreate()
        createModel()
//        createWidget()
    }

    private fun createModel() {
        val shader = engine.shaderFactory.createShader(
            vertexShaderName = "vertex_3d",
            fragmentShaderName = "fragment_3d"
        )

        val textureSlot1 = shader.shaderOwner.createSampler2D(name = "diffuseSampler1")
        val textureSlot2 = shader.shaderOwner.createSampler2D(name = "diffuseSampler2")

        shader.shaderOwner.run {
            createTexture2D(fileName = "lava.jpg", slot = textureSlot1)
            createTexture2D(fileName = "logo.png", slot = textureSlot2)
        }

        engine.objParser.parse("deagle")

        val positionAttribute = Attribute3f(name = "position")
        val colorAttribute = Attribute4f(name = "color", data = Color4f(red = 0.5f, blue = 0.5f, alpha = 0.1f))
        val coordinateAttribute = Attribute2f(name = "coordinate")
        val slotAttribute = Attribute1f(name = "slot", data = textureSlot1.toFloat())

        val transformAttribute1 = Attribute16f(
            name = "transform",
            type = Attribute.INSTANCE_TYPE,
            data = TransformMatrix4f(position = Vector3f(-15f, 0f, 0f))
        )

        val transformAttribute2 = Attribute16f(
            name = "transform",
            type = Attribute.INSTANCE_TYPE,
            data = TransformMatrix4f()
        )

        val transformAttribute3 = Attribute16f(
            name = "transform",
            type = Attribute.INSTANCE_TYPE,
            data = TransformMatrix4f(position = Vector3f(15f, 0f, 0f))
        )

        shader.shaderOwner.run {
            addVertexAttribute(positionAttribute)
            addVertexAttribute(coordinateAttribute)
            addVertexAttribute(slotAttribute)
//            addInstanceAttribute(transformAttribute1)
        }

        val positions = engine.objParser.getPositions()
        val coordinates = engine.objParser.getTextureCoordinates()

        val deagleMesh = Mesh().apply {
            indices = Updatable(data = engine.objParser.getIndices())

            addVertexAttribute(positionAttribute)
            addVertexAttribute(coordinateAttribute)
            addVertexAttribute(slotAttribute)

            allocateVertices(engine.objParser.getVertexCount())
            fillVertices(
                attributeOffset = positionAttribute.offset,
                attributeSize = positionAttribute.size(),
                attributeData = positions
            )
            fillVertices(
                attributeOffset = coordinateAttribute.offset,
                attributeSize = coordinateAttribute.size(),
                attributeData = coordinates
            )
            fillVertices(
                attributeOffset = slotAttribute.offset,
                attributeData = floatArrayOf(textureSlot1.toFloat()),
                vertexEnd = getVertexCount() / 2
            )
            fillVertices(
                attributeOffset = slotAttribute.offset,
                attributeData = floatArrayOf(textureSlot2.toFloat()),
                vertexStart = getVertexCount() / 2
            )

//            addMeshAttribute(transformAttribute1)
//
//            allocateMeshData(3)
//            fillMeshData(meshStart = 0, attributeOffset = transformAttribute1.offset, transformAttribute1.data)
//            fillMeshData(meshStart = 1, attributeOffset = transformAttribute2.offset, transformAttribute2.data)
//            fillMeshData(meshStart = 2, attributeOffset = transformAttribute3.offset, transformAttribute3.data)
        }

        val deagleMeshComponent = MeshComponent(deagleMesh)

        val deagle1 = Entity()

        val deagleEntityGroup = EntityGroup().apply {
            putComponent(shader)
            putComponent(deagleMeshComponent)
            addEntity(deagle1)
        }

        engine.addEntityGroup(systemTag = Render3dSystem::class.java.simpleName, entityGroup = deagleEntityGroup)
    }

    private fun createWidget() {
        val widgetShader = engine.shaderFactory.createShader(
            vertexShaderName = "widget_vertex",
            fragmentShaderName = "widget_fragment"
        )

        val positionAttribute = Attribute3f(name = "position")
        val colorAttribute = Attribute4f(name = "color")

        widgetShader.shaderOwner.run {
            addVertexAttribute(positionAttribute)
            addVertexAttribute(colorAttribute)
        }

        val widgetGroup = EntityGroup()

        val widgetMesh = Square().apply {
            addVertexAttribute(positionAttribute)
            addVertexAttribute(colorAttribute)
        }

        val widgetMeshComponent = MeshComponent(mesh = widgetMesh)

        widgetGroup.run {
            putComponent(widgetShader)
            putComponent(widgetMeshComponent)
        }

        engine.addEntityGroup(systemTag = Render2dSystem::class.java.simpleName, entityGroup = widgetGroup)
    }

}