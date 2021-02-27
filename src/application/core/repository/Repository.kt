package application.core.repository

abstract class Repository<T>(protected val dataList: ArrayList<T>) {

    abstract fun onCreate()

    fun add(data: T) {
        dataList.add(data)
    }

    fun first(): T = dataList[0]

    open fun clean() {
        dataList.clear()
    }

    fun getDataList(): List<T> = dataList

}