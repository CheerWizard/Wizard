package application.graphics.core.material

import application.graphics.shader.uniforms.UFloat

class Specular(
    var shining: UFloat = UFloat(value = 2f),
    var reflectivity: UFloat = UFloat(value = 1f),
    var brightness: UFloat = UFloat(),
)