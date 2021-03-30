package engine.core.collection

import org.joml.Vector2f

class Vector2fList : ArrayList<Vector2f>() {

    fun toFloatArray(): FloatArray {
        val floatArray = FloatArray(size * 2)

        var i = 0
        for (vector2f in this) {
            floatArray[i++] = vector2f.x
            floatArray[i++] = vector2f.y
        }

        return floatArray
    }

}