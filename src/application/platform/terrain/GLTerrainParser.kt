package application.platform.terrain

import application.graphics.terrain.TerrainParser
import application.graphics.geometry.VertexBuffer
import application.platform.vertex.GLVertexBuffer

class GLTerrainParser : TerrainParser() {

    override fun getVertexBuffer(vertexName: String): VertexBuffer = GLVertexBuffer()

    override fun getTextureBuffer(vertexName: String): VertexBuffer = GLVertexBuffer()

    override fun getNormalBuffer(vertexName: String): VertexBuffer = GLVertexBuffer()

}