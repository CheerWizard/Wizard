package application.graphics.component

import application.graphics.vertex.IndexBuffer
import application.graphics.vertex.VertexArray
import application.graphics.vertex.VertexBuffer

class CubeMesh(
    vertexArray: VertexArray,
    vertexBuffer: VertexBuffer,
    indexBuffer: IndexBuffer
) : MeshComponent(
    vertexArray = vertexArray,
    vertexBuffer = vertexBuffer,
    indexBuffer = indexBuffer
) {

    init {

    }

    fun createCube() {
        createCubeIndices()
        createCubeVertices()
        createCubeTextures()
        createCubeNormals()
    }

    private fun createCubeVertices() {
        floatArrayOf(
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f
        )
    }

    private fun createCubeTextures() {
        floatArrayOf(
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f,
            0f, 1f,
            1f, 1f,
            1f, 0f
        )
    }

    private fun createCubeIndices() {
        val indices = intArrayOf(
            0, 1, 3,
            3, 1, 2,
            4, 5, 7,
            7, 5, 6,
            8, 9, 11,
            11, 9, 10,
            12, 13, 15,
            15, 13, 14,
            16, 17, 19,
            19, 17, 18,
            20, 21, 23,
            23, 21, 22
        )
        indexBuffer.addIndex(indices)
    }

    private fun createCubeNormals() {
        floatArrayOf(
            -1.0f, 0.0f, 0.0f, // Left Side
            0.0f, 0.0f, -1.0f, // Back Side
            0.0f,-1.0f, 0.0f, // Bottom Side
            0.0f, 0.0f, -1.0f, // Back Side
            -1.0f, 0.0f, 0.0f, // Left Side
            0.0f, -1.0f, 0.0f, // Bottom Side
            0.0f, 0.0f, 1.0f, // front Side
            1.0f, 0.0f, 0.0f, // right Side
            1.0f, 0.0f, 0.0f, // right Side
            0.0f, 1.0f, 0.0f, // top Side
            0.0f, 1.0f, 0.0f, // top Side
            0.0f, 0.0f, 1.0f, // front Side
        )
    }

}