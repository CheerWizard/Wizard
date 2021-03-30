package engine.graphics.shader.uniforms

class UFloat(
    var name: String = "",
    var value: Float = 0f
) {

    var isUpdated = true

    fun applyChanges() {
        isUpdated = true
    }

    fun discardChanges() {
        isUpdated = false
    }

}