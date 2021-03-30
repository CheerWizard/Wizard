package engine.core.repository

import engine.core.Destroyable

abstract class DestroyableRepository<T : Destroyable>(dataList: ArrayList<T>) : Repository<T>(dataList = dataList) {
    override fun clean() {
        for (destroyable in dataList) {
            destroyable.onDestroy()
        }
        super.clean()
    }
}