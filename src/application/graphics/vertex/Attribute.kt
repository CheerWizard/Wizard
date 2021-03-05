package application.graphics.vertex

abstract class Attribute(
    var name: String = "",
    var location: Int = 0
) {
    abstract fun size(): Int
    fun byteSize(): Int = size() * Float.SIZE_BYTES
}