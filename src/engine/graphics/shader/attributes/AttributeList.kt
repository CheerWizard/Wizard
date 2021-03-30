package engine.graphics.shader.attributes

class AttributeList : ArrayList<Attribute>() {

    private var totalSize = 0

    private var totalData = FloatArray(0)

    fun getTotalSize(): Int = totalSize

    fun getByteSize(): Int = totalSize * Float.SIZE_BYTES

    fun getTotalData(): FloatArray = totalData

    override fun add(element: Attribute): Boolean {
        element.offset = totalSize
        totalSize += element.size() * element.count
        totalData += element.data
        return super.add(element)
    }

    override fun add(index: Int, element: Attribute) {
        element.offset = totalSize
        totalSize += element.size() * element.count
        totalData += element.data
        super.add(index, element)
    }

    override fun clear() {
        totalSize = 0
        totalData = FloatArray(0)
        super.clear()
    }

    fun getUpdatedAttributes(): List<Attribute> {
        val attributes = ArrayList<Attribute>()

        for (attribute in this) {
            if (attribute.isUpdated) {
                attributes.add(attribute)
            }
        }

        return attributes
    }

}