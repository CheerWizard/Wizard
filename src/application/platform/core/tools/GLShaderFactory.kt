package application.platform.core.tools

import application.graphics.shader.ShaderComponent
import application.graphics.tools.ShaderFactory
import application.platform.shader.GLFragmentShader
import application.platform.shader.GLShaderOwner
import application.platform.shader.GLVertexShader

class GLShaderFactory : ShaderFactory {
    override fun createShader(vertexShaderName: String, fragmentShaderName: String): ShaderComponent = ShaderComponent(
        shaderOwner = GLShaderOwner(
            vertexShader = GLVertexShader(vertexShaderName).readFile(),
            fragmentShader = GLFragmentShader(fragmentShaderName).readFile()
        )
    )
}