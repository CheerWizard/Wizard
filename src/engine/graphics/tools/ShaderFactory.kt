package engine.graphics.tools

import engine.graphics.shader.ShaderComponent

interface ShaderFactory {
    fun createShader(vertexShaderName: String, fragmentShaderName: String): ShaderComponent
}