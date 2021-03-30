package engine.graphics.shader.uniforms

import engine.core.Updatable

class UIntegerArray(
    var name: String = "",
    data: IntArray = IntArray(0)
) : Updatable<IntArray>(data = data)