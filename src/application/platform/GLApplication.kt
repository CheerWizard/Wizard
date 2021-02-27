package application.platform

import application.Application
import application.core.ecs.Entity
import application.core.ecs.EntityGroup
import application.core.math.TransformMatrix4f
import application.graphics.render.Render3dSystem
import application.graphics.component.CameraComponent
import application.graphics.component.TextureComponent
import application.graphics.obj.ObjParser
import application.graphics.shader.ShaderComponent
import application.platform.mesh.GLMeshComponent
import application.platform.mesh.GLVertex
import application.platform.mesh.GLVertexBuffer
import application.platform.obj.GLObjParser
import application.platform.shader.GLFragmentShader
import application.platform.shader.GLShaderOwner
import application.platform.shader.GLVertexShader
import application.platform.texture.GLTextureCubeMap
import org.lwjgl.glfw.GLFW

abstract class GLApplication(title: String = "OpenGL") : Application(title = title) {

    override val objParser: ObjParser = GLObjParser()

    protected open lateinit var cameraComponent: CameraComponent

    override fun onCreate() {
        println("onCreate()")
        cameraComponent = CameraComponent(
            transformMatrix4f = TransformMatrix4f(
                name = "camera"
            )
        )
        createSkyBox()
    }

    override fun onWindowResized(width: Float, height: Float) {
        engine.getSystem<Render3dSystem>(Render3dSystem.ID).applyWindowAspectRatio(width, height)
    }

    private var x = window.getCursorCenterX()
    private var y = window.getCursorCenterY()

    override fun onCursorCoordinatesChanged(x: Float, y: Float) {
        super.onCursorCoordinatesChanged(x, y)

        if (!inputController.isMouseHold()) return

        val dx = x - this.x
        val dy = y - this.y

        updateCamera {
            rotate(x = dx, y = dy)
        }

        this.x = x
        this.y = y
    }

    private fun createSkyBox() {
        val size = 1f
        val vertices = floatArrayOf(
            -size,  size, -size,
            -size, -size, -size,
            size, -size, -size,
            size, -size, -size,
            size,  size, -size,
            -size,  size, -size,

            -size, -size,  size,
            -size, -size, -size,
            -size,  size, -size,
            -size,  size, -size,
            -size,  size,  size,
            -size, -size,  size,

            size, -size, -size,
            size, -size,  size,
            size,  size,  size,
            size,  size,  size,
            size,  size, -size,
            size, -size, -size,

            -size, -size,  size,
            -size,  size,  size,
            size,  size,  size,
            size,  size,  size,
            size, -size,  size,
            -size, -size,  size,

            -size,  size, -size,
            size,  size, -size,
            size,  size,  size,
            size,  size,  size,
            -size,  size,  size,
            -size,  size, -size,

            -size, -size, -size,
            -size, -size,  size,
            size, -size, -size,
            size, -size, -size,
            -size, -size,  size,
            size, -size,  size
        )

        val vertexBuffer = GLVertexBuffer(
            vertex = GLVertex(
                name = "skyboxVertices",
                typeSize = 3
            ),
            data = vertices
        )

        val meshComponent = GLMeshComponent().apply {
            addVertexBuffer(vertexBuffer)
        }

        val textureComponent = TextureComponent().apply {
            addTexture(
                GLTextureCubeMap(
                    uniformName = "skyboxCubeSampler",
                    storagePath = "res/skybox"
                ).create(
                    arrayOf(
                        "right.png",
                        "left.png",
                        "top.png",
                        "bottom.png",
                        "back.png",
                        "front.png",
                    )
                )
            )
        }

        val shaderComponent = ShaderComponent(
            shaderOwner = GLShaderOwner(
                vertexShader = GLVertexShader("skybox_vertex.glsl").readFile(),
                fragmentShader = GLFragmentShader("skybox_fragment.glsl").readFile()
            )
        )

        val skyGroup = EntityGroup().apply {
            putComponent(meshComponent)
            putComponent(textureComponent)
            putComponent(shaderComponent)
            putComponent(cameraComponent)
            addEntity(
                Entity(
                    transformation = TransformMatrix4f(
                        name = "skyboxTransform",
                        scale = 500f
                    )
                )
            )
        }

        skyboxId = engine.addEntityGroup(systemId = Render3dSystem.ID, skyGroup)
    }

    override fun onUpdate() {
        super.onUpdate()
        rotateSkybox()
    }

    private var skyboxId = 0

    protected open fun rotateSkybox() {
        engine.getEntity<Render3dSystem>(systemId = Render3dSystem.ID, entityGroupId = skyboxId, entityId = 0).apply {
            transformation.rotation.addYDegree(0.01)
            transformation.apply()
        }
    }

    override fun bindInputController() {
        super.bindInputController()
        inputController.run {
            bindMouseScrollDownEvent { zoomOutCamera() }
            bindMouseScrollUpEvent { zoomInCamera() }

            bindMouseHoldEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT) {}
            bindMouseReleasedEvent(GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                x = window.getCursorCenterX()
                y = window.getCursorCenterY()
            }

            bindKeyHoldEvent(GLFW.GLFW_KEY_Q) { zoomOutCamera() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_E) { zoomInCamera() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_W) { moveCameraUp() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_S) { moveCameraDown() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_A) { moveCameraLeft() }
            bindKeyHoldEvent(GLFW.GLFW_KEY_D) { moveCameraRight() }
        }
    }

    private fun updateCamera(updateFunction : CameraComponent.() -> Unit) {
        updateFunction.invoke(cameraComponent)
    }

    private fun zoomOutCamera() {
        updateCamera {
            zoomOut()
        }
    }

    private fun zoomInCamera() {
        updateCamera {
            zoomIn()
        }
    }

    private fun moveCameraLeft() {
        updateCamera {
            moveLeft()
        }
    }

    private fun moveCameraRight() {
        updateCamera {
            moveRight()
        }
    }

    private fun moveCameraUp() {
        updateCamera {
            moveUp()
        }
    }

    private fun moveCameraDown() {
        updateCamera {
            moveDown()
        }
    }

}