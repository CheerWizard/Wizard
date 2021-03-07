package application.graphics.geometry

class Attribute4f(name: String, location: Int) : Attribute(name = name, location = location) {
    override fun size(): Int = 4
}

class Attribute3f(name: String, location: Int) : Attribute(name = name, location = location) {
    override fun size(): Int = 3
}

class Attribute2f(name: String, location: Int) : Attribute(name = name, location = location) {
    override fun size(): Int = 2
}

class Attribute1f(name: String, location: Int) : Attribute(name = name, location = location) {
    override fun size(): Int = 1
}