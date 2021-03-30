package engine.graphics.math

class Projection3dMatrix4f(
    name: String,
    screenWidth: Float,
    screenHeight: Float,
    var fieldOfView: Float = DEFAULT_FIELD_OF_VIEW,
    var zNear: Float = DEFAULT_Z_NEAR,
    var zFar: Float = DEFAULT_Z_FAR
) : ProjectionMatrix4f(
    name = name,
    screenWidth = screenWidth,
    screenHeight = screenHeight
) {

    companion object {
        private const val DEFAULT_FIELD_OF_VIEW: Float = 60f
        private const val DEFAULT_Z_NEAR: Float = 0.01f
        private const val DEFAULT_Z_FAR: Float = 1000f
    }

    init {
        applyChanges()
    }

    override fun applyChanges() {
        super.applyChanges()
        identity()
        perspective(
            Math.toRadians(fieldOfView.toDouble()).toFloat(),
            screenWidth / screenHeight,
            zNear,
            zFar
        )
    }

}