package engine.core

open class Updatable<T>(
    var isUpdated: Boolean = true,
    var data: T
) {
    open fun applyChanges() {
        isUpdated = true
    }

    fun discardChanges() {
        isUpdated = false
    }

}