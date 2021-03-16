package application.core.collection

import org.joml.Vector3f

class Vector3fList : ArrayList<Vector3f>() {

    fun toFloatArray(): FloatArray {
        val floatArray = FloatArray(size * 3)

        var i = 0
        for (vector3f in this) {
            floatArray[i++] = vector3f.x
            floatArray[i++] = vector3f.y
            floatArray[i++] = vector3f.z
        }

        return floatArray
    }

}