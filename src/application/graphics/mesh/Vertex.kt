package application.graphics.mesh

abstract class Vertex(
    val name: String,
    val typeSize: Int
) {
    var attribute: Int = 0

    abstract fun enableAttribute()
    abstract fun disableAttribute()
}