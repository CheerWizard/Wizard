package application.graphics.geometry

class Mesh(
    val vertexCount: Int = DEFAULT_VERTEX_COUNT,
    val vertexSize: Int = DEFAULT_VERTEX_SIZE
) {

    fun size(): Int = vertexSize * vertexCount

    companion object {
        const val DEFAULT_VERTEX_COUNT = 1000
        const val DEFAULT_VERTEX_SIZE = Vertex.MAX_SIZE
        const val MAX_SIZE = DEFAULT_VERTEX_COUNT * DEFAULT_VERTEX_SIZE
    }

}