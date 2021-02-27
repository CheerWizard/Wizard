package application.core

interface Binder : Destroyable {
    fun onBind()
    fun onUnbind()
}