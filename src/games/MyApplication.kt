package games

import engine.core.ecs.Engine
import engine.graphics.core.mesh.MeshComponent
import engine.graphics.core.scene.LightComponent
import engine.graphics.render.Render3dSystem
import engine.graphics.shader.uniforms.UColor3f
import engine.graphics.shader.uniforms.UVector3f
import engine.platform.GLEngine
import engine.platform.GraphicsApplication
import engine.platform.core.buffers.GLVertexBuffer
import org.lwjgl.glfw.GLFW

class MyApplication : GraphicsApplication() {

    override val engine: Engine = GLEngine(title = "MyApplication", client = this)

    override var cursorX: Float = engine.window.getCursorCenterX()
    override var cursorY: Float = engine.window.getCursorCenterY()

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

    private var plateId = 0

    override fun bindIOController() {
        super.bindIOController()
        engine.ioController.run {
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
            systemTag = Render3dSystem::class.java.simpleName,
            entityGroupId = plateId,
            componentId = MeshComponent.ID
        )?.apply {
        }
    }

    private fun moveLight(x: Float = 0f, y: Float = 0f, z: Float = 0f) {
        sunLightComponent.position.run {
            data.x += x
            data.y += y
            data.z += z
            applyChanges()
        }
    }

}