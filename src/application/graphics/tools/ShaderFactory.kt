package application.graphics.tools

import application.graphics.shader.ShaderComponent

interface ShaderFactory {
    fun createShader(vertexShaderName: String, fragmentShaderName: String): ShaderComponent
}