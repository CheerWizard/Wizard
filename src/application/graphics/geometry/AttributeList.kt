package application.graphics.geometry

class AttributeList : ArrayList<Attribute>() {

    var totalSize = 0

    override fun add(element: Attribute): Boolean {
        totalSize += element.size()
        return super.add(element)
    }

    override fun add(index: Int, element: Attribute) {
        totalSize += element.size()
        super.add(index, element)
    }

    override fun clear() {
        totalSize = 0
        super.clear()
    }

}