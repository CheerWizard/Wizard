package engine.graphics.render

abstract class Render2dSystem(screenWidth: Float, screenHeight: Float) : RenderSystem(screenWidth = screenWidth, screenHeight = screenHeight) {

    override val tag: String = Render2dSystem::class.java.simpleName
    override val priority: Byte = 0

    override fun prepareComponents(entityGroupId: Int) {
    }

    override fun prepareSceneComponents(entityGroupId: Int) {
    }

    override fun updateSceneComponents() {
        isCullFaceEnabled = true
        onEnableCullFace()
    }

}