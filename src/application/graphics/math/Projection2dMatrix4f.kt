package application.graphics.math

class Projection2dMatrix4f(
    name: String,
    screenHeight: Float,
    screenWidth: Float,
    val left: Float = 0f,
    val top: Float = 0f
) : ProjectionMatrix4f(name = name, screenHeight = screenHeight, screenWidth = screenWidth) {

    init {
        apply()
    }

    override fun apply() {
        identity()
        ortho2D(left, screenWidth, screenHeight, top)
    }

}