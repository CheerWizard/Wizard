package engine.graphics.math

class Projection2dMatrix4f(
    name: String,
    screenHeight: Float,
    screenWidth: Float,
    val left: Float = 0f,
    val top: Float = 0f
) : ProjectionMatrix4f(name = name, screenHeight = screenHeight, screenWidth = screenWidth) {

    init {
        applyChanges()
    }

    override fun applyChanges() {
        super.applyChanges()
        identity()
        ortho(
            0f,
            screenWidth,
            screenHeight,
            0f,
            -1f,
            1f
        )
    }

}