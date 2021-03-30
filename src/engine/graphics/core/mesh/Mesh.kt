package engine.graphics.core.mesh

import engine.core.Destroyable
import engine.core.Updatable
import engine.graphics.shader.attributes.Attribute
import engine.graphics.shader.attributes.AttributeList

open class Mesh(
        open var indices: Updatable<IntArray> = Updatable(data = IntArray(0)),
        open var vertices: VertexList = VertexList(),
        var attributes: AttributeList = AttributeList()
) : Destroyable {

        var vertexStart = 0
        var indexStart = 0

        private var data = FloatArray(0)

        fun discardChanges() {
                indices.discardChanges()
                vertices.discardChanges()
        }

        fun getVertexCount(): Int = vertices.getVertexCount()

        fun getIndexCount(): Int = indices.data.size

        fun getAttributesSize(): Int = attributes.getTotalSize()

        fun getVerticesData(): FloatArray = vertices.getVertices()

        fun addVertexAttribute(vertexAttribute: Attribute) {
                vertices.addAttribute(vertexAttribute)
        }

        fun setVertexAttributes(vertexAttributes: AttributeList) {
                vertices.attributes = vertexAttributes
        }

        fun addMeshAttribute(meshAttribute: Attribute) {
                attributes.add(meshAttribute)
        }

        fun getMeshData(): FloatArray = data

        fun getMeshUpdatedAttributes(): List<Attribute> = attributes.getUpdatedAttributes()

        fun addVertices(vertices: FloatArray) {
                this.vertices.addVertices(vertices)
        }

        fun fillVertices(attributeOffset: Int, attributeData: FloatArray, vertexStart: Int = 0, vertexEnd: Int = getVertexCount()) {
                vertices.fillVertices(attributeOffset, attributeData, vertexStart, vertexEnd)
        }

        fun fillVertices(attributeOffset: Int, attributeData: FloatArray, attributeSize: Int) {
                vertices.fillVertices(attributeOffset, attributeData, attributeSize)
        }

        fun allocateVertices(vertexCount: Int) {
                vertices.allocateVertices(vertexCount)
        }

        fun allocateMeshData(meshCount: Int) {
                data = FloatArray(meshCount * attributes.getTotalSize())
        }

        fun fillMeshData(meshStart: Int, meshEnd: Int, attributeOffset: Int, attributeData: FloatArray) {
                val meshSize = attributes.getTotalSize()
                var start = meshStart * meshSize
                val end = meshEnd * meshSize
                while (start < end) {
                        val i = start + attributeOffset
                        for (j in attributeData.indices) {
                                data[i + j] = attributeData[j]
                        }
                        start += meshSize
                }
        }

        fun fillMeshData(meshStart: Int, attributeOffset: Int, attributeData: FloatArray) {
                fillMeshData(
                        meshStart = meshStart,
                        meshEnd = meshStart + 1,
                        attributeOffset = attributeOffset,
                        attributeData = attributeData
                )
        }

        override fun onDestroy() {
                vertices.onDestroy()
                attributes.clear()
        }

}