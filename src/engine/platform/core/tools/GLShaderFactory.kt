package engine.platform.core.tools

import engine.graphics.shader.ShaderComponent
import engine.graphics.tools.ShaderFactory
import engine.platform.shader.GLFragmentShader
import engine.platform.shader.GLShaderOwner
import engine.platform.shader.GLVertexShader

class GLShaderFactory : ShaderFactory {
    override fun createShader(vertexShaderName: String, fragmentShaderName: String): ShaderComponent = ShaderComponent(
        shaderOwner = GLShaderOwner(
            vertexShader = GLVertexShader(vertexShaderName).readFile(),
            fragmentShader = GLFragmentShader(fragmentShaderName).readFile()
        )
    )
}