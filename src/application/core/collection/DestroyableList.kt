package application.core.collection

import application.core.Destroyable

open class DestroyableList<T: Destroyable> : ArrayList<T>() {

    override fun remove(element: T): Boolean {
        return super.remove(element.apply {
            onDestroy()
        })
    }

    override fun removeAt(index: Int): T {
        return super.removeAt(index).apply {
            onDestroy()
        }
    }

    override fun clear() {
        for (entity in this) {
            entity.onDestroy()
        }
        super.clear()
    }

}