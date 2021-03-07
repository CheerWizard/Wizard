package application.graphics.geometry

abstract class Attribute(
        var name: String,
        var location: Int
) {
    abstract fun size(): Int
}