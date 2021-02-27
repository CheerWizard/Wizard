package application.core.collection

import application.core.Destroyable

open class DestroyableMap<K, V: Destroyable> : HashMap<K, V>() {

    override fun remove(key: K): V? {
        return super.remove(key)?.apply {
            onDestroy()
        }
    }

    override fun remove(key: K, value: V): Boolean {
        return super.remove(key, value.apply {
            onDestroy()
        })
    }

    override fun clear() {
        for (entity in values) {
            entity.onDestroy()
        }
        super.clear()
    }
}