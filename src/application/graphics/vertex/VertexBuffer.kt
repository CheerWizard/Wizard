package application.graphics.vertex

import application.core.Destroyable
import org.lwjgl.BufferUtils
import java.nio.FloatBuffer

abstract class VertexBuffer(
    capacity: Int = DEFAULT_CAPACITY
) : Destroyable {

    companion object {
        const val DEFAULT_CAPACITY: Int = 1024 / Float.SIZE_BYTES
    }

    abstract var id: Int

    abstract var polygonMode: Int

    protected val attributes: HashMap<String, Attribute> = HashMap()

    val data: FloatBuffer = BufferUtils.createFloatBuffer(capacity)

    fun capacity(): Int = data.capacity()

    fun position(): Int = data.position()

    fun setPosition(newPosition: Int) = data.position(newPosition)

    fun putAttribute(attribute: Attribute) {
        attributes[attribute.name] = attribute
    }

    fun putAttributes(attributes: Array<Attribute>) {
        for (attribute in attributes) {
            putAttribute(attribute)
        }
    }

    fun getAttributes(): Collection<Attribute> = attributes.values

    fun removeAttributes() {
        attributes.clear()
    }

    abstract fun enableAttributes()
    abstract fun disableAttributes()

    fun addVertex(vertex: Vertex) {
        data.run {
            put(vertex.position.x)
            put(vertex.position.y)
            put(vertex.position.z)

            put(vertex.color.x)
            put(vertex.color.y)
            put(vertex.color.z)
            put(vertex.color.w)

            put(vertex.textureCoordinate.x)
            put(vertex.textureCoordinate.y)

            put(vertex.normal.x)
            put(vertex.normal.y)
            put(vertex.normal.z)

            put(vertex.textureSlot)
        }
    }

    fun addVertex(vertexArray: FloatArray) {
        data.put(vertexArray)
    }

    fun flip() {
        data.flip()
    }

    fun clear() {
        data.clear()
    }

    abstract fun create()
    abstract fun bind()
    abstract fun unbind()

    override fun onDestroy() {
        removeAttributes()
        clear()
    }

}