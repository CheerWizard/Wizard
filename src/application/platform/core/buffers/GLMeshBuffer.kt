package application.platform.core.buffers

import application.graphics.core.buffers.MeshBuffer
import application.graphics.shader.Attribute3f

class GLMeshBuffer : MeshBuffer(indexBuffer = GLIndexBuffer()) {

    override fun createNormalBuffer(name: String) {
        normalsBuffer = GLVertexBuffer(attribute = Attribute3f(
            name = name
        ))
    }

    override fun createPositionBuffer(name: String) {
        positionBuffer = GLVertexBuffer(attribute = Attribute3f(
            name = name
        ))
    }

}