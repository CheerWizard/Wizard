package engine.graphics.shader.uniforms

import org.joml.Matrix4f

open class UMatrix4f(var name: String = "") : Matrix4f() {

    var isUpdated = true

    open fun applyChanges() {
        isUpdated = true
    }

    open fun discardChanges() {
        isUpdated = false
    }

}