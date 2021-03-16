package application.graphics.shader

class Attribute4f(name: String = "", location: Int = 0) : Attribute(name = name, location = location) {
    override fun size(): Int = 4
}

class Attribute3f(name: String = "", location: Int = 0) : Attribute(name = name, location = location) {
    override fun size(): Int = 3
}

class Attribute2f(name: String = "", location: Int = 0) : Attribute(name = name, location = location) {
    override fun size(): Int = 2
}

class Attribute1f(name: String = "", location: Int = 0) : Attribute(name = name, location = location) {
    override fun size(): Int = 1
}