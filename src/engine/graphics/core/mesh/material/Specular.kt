package engine.graphics.core.mesh.material

import engine.graphics.shader.uniforms.UFloat

class Specular(
    var shining: UFloat = UFloat(value = 2f),
    var reflectivity: UFloat = UFloat(value = 1f),
    var brightness: UFloat = UFloat(),
)