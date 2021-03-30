package engine.graphics.shader.attributes

abstract class Attribute(
    var name: String,
    var location: Int,
    var count: Int = 1,
    var type: Int = VERTEX_TYPE,
    var data: FloatArray
) {
    companion object {
        const val VERTEX_TYPE = 0
        const val INSTANCE_TYPE = 1
    }

    var offset = 0

    var isUpdated = false

    fun applyChanges() {
        isUpdated = true
    }

    fun discardChanges() {
        isUpdated = false
    }

    fun size(): Int = data.size / count

}