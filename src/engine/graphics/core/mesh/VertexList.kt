package engine.graphics.core.mesh

import engine.core.Destroyable
import engine.graphics.shader.attributes.Attribute
import engine.graphics.shader.attributes.AttributeList

class VertexList : Destroyable {

    var attributes = AttributeList()

    private var vertices = FloatArray(0)

    var isUpdated = false

    fun applyChanges() {
        isUpdated = true
    }

    fun discardChanges() {
        isUpdated = false
    }

    fun addAttribute(attribute: Attribute) {
        attributes.add(attribute)
    }

    fun addVertex(vertex: FloatArray) {
        vertices += vertex
    }

    fun addVertices(vertices: FloatArray) {
        this.vertices += vertices
    }

    fun getVertices(): FloatArray = vertices

    fun getVertexCount(): Int = vertices.size / attributes.getTotalSize()

    fun fillVertices(
        attributeOffset: Int,
        attributeData: FloatArray,
        vertexStart: Int,
        vertexEnd: Int
    ) {
        val vertexSize = attributes.getTotalSize()
        var start = vertexStart * vertexSize
        val end = vertexEnd * vertexSize

        while (start < end) {
            val i = start + attributeOffset

            for (j in attributeData.indices) {
                vertices[i + j] = attributeData[j]
            }

            start += vertexSize
        }
    }

    fun allocateVertices(vertexCount: Int) {
        vertices = FloatArray(vertexCount * attributes.getTotalSize())
    }

    fun fillVertices(attributeOffset: Int, attributeData: FloatArray, attributeSize: Int) {
        val vertexSize = attributes.getTotalSize()
        var i = 0
        var j = 0
        while (i < vertices.size) {

            var k = 0
            while (k < attributeSize) {
                vertices[i + attributeOffset + k] = attributeData[j]
                j++
                k++
            }

            i += vertexSize
        }
    }

    override fun onDestroy() {
        vertices = FloatArray(0)
        attributes.clear()
    }

}