package application.graphics.shader

abstract class Attribute(
        var name: String,
        var location: Int
) {
    abstract fun size(): Int
}