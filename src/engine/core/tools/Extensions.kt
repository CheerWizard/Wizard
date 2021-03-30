package engine.core.tools

fun IntArray.toFloatArray() : FloatArray {
    val floatArray = FloatArray(size)

    for (i in 0 until size) {
        floatArray[i] = this[i].toFloat()
    }

    return floatArray
}