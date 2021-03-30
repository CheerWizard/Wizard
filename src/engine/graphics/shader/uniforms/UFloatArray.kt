package engine.graphics.shader.uniforms

import engine.core.Updatable

class UFloatArray(
    var name: String = "",
    data: FloatArray = FloatArray(0)
) : Updatable<FloatArray>(data = data)