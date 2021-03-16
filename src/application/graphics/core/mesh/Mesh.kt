package application.graphics.core.mesh

import application.graphics.core.material.Material

open class Mesh(
        open val positions: FloatArray,
        open val normals: FloatArray? = null,
        open val indices: IntArray
) {
        fun createMaterial() : Material = Material(vertexCount = positions.size / 3)
}