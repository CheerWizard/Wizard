package application.core.repository

import application.core.Destroyable

abstract class DestroyableRepository<T : Destroyable>(dataList: ArrayList<T>) : Repository<T>(dataList = dataList) {
    override fun clean() {
        for (destroyable in dataList) {
            destroyable.onDestroy()
        }
        super.clean()
    }
}