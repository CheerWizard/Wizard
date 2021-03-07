package application.platform

import application.Application
import application.core.ecs.Entity
import application.core.ecs.EntityGroup
import application.core.math.TransformMatrix4f
import application.core.math.Vector3f
import application.graphics.component.CameraComponent
import application.graphics.geometry.Attribute2f
import application.graphics.geometry.Attribute3f
import application.graphics.material.MaterialBuffer
import application.graphics.mesh.MeshComponent
import application.graphics.material.MaterialComponent
import application.graphics.mesh.MeshBuffer
import application.graphics.obj.ObjParser
import application.graphics.render.Render3dSystem
import application.graphics.shader.ShaderComponent
import application.graphics.terrain.TerrainParser
import application.platform.shader.GLFragmentShader
import application.platform.shader.GLShaderOwner
import application.platform.shader.GLVertexShader
import application.platform.texture.GLTextureCubeMap
import application.platform.geometry.GLIndexBuffer
import application.platform.geometry.GLVertexArray
import application.platform.geometry.GLVertexBuffer
import org.lwjgl.glfw.GLFW

abstract class GLApplication(title: String = "OpenGL") : Application(title = title) {

    protected val objParser: ObjParser = ObjParser()
    protected val terrainParser: TerrainParser = TerrainParser()

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

        val meshBuffer = MeshBuffer(
                positionBuffer = GLVertexBuffer(
                        attribute = Attribute3f(
                                name = "position",
                                location = 0
                        )
                ),
                indexBuffer = GLIndexBuffer()
        ).apply {
            normalsBuffer = GLVertexBuffer(
                    attribute = Attribute3f(
                            name = "normal",
                            location = 1
                    )
            )
        }

        val meshComponent = MeshComponent(
            vertexArray = GLVertexArray()
        ).apply {
            addMeshBuffer(meshBuffer)
        }

        val materialBuffer = MaterialBuffer(
                coordinatesBuffer = GLVertexBuffer(
                        attribute = Attribute2f(
                                name = "coordinate",
                                location = 2
                        )
                ),
                textureBuffer =  GLTextureCubeMap(
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

        val textureComponent = MaterialComponent().apply {
            addMaterialBuffer(materialBuffer)
        }

        val shaderComponent = ShaderComponent(
            shaderOwner = GLShaderOwner(
                vertexShader = GLVertexShader("cube_vertex.glsl").readFile(),
                fragmentShader = GLFragmentShader("cube_fragment.glsl").readFile()
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
                        scalar = Vector3f(x = 500f, y = 500f, z = 500f)
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