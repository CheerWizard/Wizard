package sandbox

import application.core.ecs.Engine
import application.core.ecs.Entity
import application.core.ecs.EntityGroup
import application.graphics.core.material.MaterialComponent
import application.graphics.core.mesh.MeshComponent
import application.graphics.geometry.Square
import application.graphics.math.Color4f
import application.graphics.render.Render3dSystem
import application.platform.GLEngine
import application.platform.GraphicsApplication

class SandboxApplication : GraphicsApplication("SandBox") {

    override val engine: Engine = GLEngine(this)

    override fun onCreate() {
        super.onCreate()

        val entityGroup = EntityGroup()

        val shaderComponent = engine.shaderFactory.createShader(
            vertexShaderName = "vertex_2d",
            fragmentShaderName = "fragment_2d"
        )

        shaderComponent.shaderOwner.run {
            createPositionBuffer("position")
            createColorsBuffer("color")
        }

        entityGroup.putComponent(shaderComponent)

        val square = Square()
        val squareComponent = MeshComponent(mesh = square)

        entityGroup.putComponent(squareComponent)

        val squareMaterial = square.createMaterial()
        squareMaterial.createColors(Color4f(red = 1f))
        val materialComponent = MaterialComponent(material = squareMaterial)

        entityGroup.putComponent(materialComponent)

        val entity = Entity()
        entityGroup.addEntity(entity)

        engine.addEntityGroup(systemId = Render3dSystem.ID, entityGroup = entityGroup)
    }

}