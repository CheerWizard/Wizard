package engine.graphics.shader.uniforms

class UInteger(
    var name: String = "",
    var value: Int = 0
) {

    var isUpdated = true

    fun applyChanges() {
        isUpdated = true
    }

    fun discardChanges() {
        isUpdated = false
    }

}