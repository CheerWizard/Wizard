package application.window

import application.core.Destroyable

class InputController : Destroyable {

    // todo migrate from HashMap to SparseArray structure in the future.

    private val keyPressedMap = HashMap<Int, () -> Unit>()
    private val keyReleasedMap = HashMap<Int, () -> Unit>()
    private val keyHoldMap = HashMap<Int, () -> Unit>()
    private val mousePressedMap = HashMap<Int, () -> Unit>()
    private val mouseReleasedMap = HashMap<Int, () -> Unit>()
    private val mouseHoldMap = HashMap<Int, () -> Unit>()

    private var mouseScrollUpEvent: () -> Unit = {}
    private var mouseScrollDownEvent: () -> Unit = {}

    private var isKeyHold = false
    private var keyHold: Int = 0
    private var isMouseHold = false
    private var mouseHold: Int = 0

    fun isMouseHold() : Boolean = isMouseHold

    fun bindKeyPressedEvent(key: Int, event: () -> Unit) {
        keyPressedMap[key] = event
    }

    fun bindKeyReleasedEvent(key: Int, event: () -> Unit) {
        keyReleasedMap[key] = event
    }

    fun bindKeyHoldEvent(key: Int, event: () -> Unit) {
        keyHoldMap[key] = event
    }

    fun bindMousePressedEvent(mouseButton: Int, event: () -> Unit) {
        mousePressedMap[mouseButton] = event
    }

    fun bindMouseReleasedEvent(mouseButton: Int, event: () -> Unit) {
        mouseReleasedMap[mouseButton] = event
    }

    fun bindMouseHoldEvent(mouseButton: Int, event: () -> Unit) {
        mouseHoldMap[mouseButton] = event
    }

    fun unbindKeyPressedEvent(key: Int) {
        keyPressedMap.remove(key)
    }

    fun unbindKeyReleasedEvent(key: Int) {
        keyReleasedMap.remove(key)
    }

    fun unbindKeyHoldEvent(key: Int) {
        keyHoldMap.remove(key)
    }

    fun unbindMousePressedEvent(mouseButton: Int) {
        mousePressedMap.remove(mouseButton)
    }

    fun unbindMouseReleasedEvent(mouseButton: Int) {
        mouseReleasedMap.remove(mouseButton)
    }

    fun unbindMouseHoldEvent(mouseButton: Int) {
        mouseHoldMap.remove(mouseButton)
    }

    fun bindMouseScrollUpEvent(mouseScrollUpEvent: () -> Unit) {
        this.mouseScrollUpEvent = mouseScrollUpEvent
    }

    fun bindMouseScrollDownEvent(mouseScrollDownEvent: () -> Unit) {
        this.mouseScrollDownEvent = mouseScrollDownEvent
    }

    fun unbindMouseScrollUpEvent() {
        this.mouseScrollUpEvent = {}
    }

    fun unbindMouseScrollDownEvent() {
        this.mouseScrollDownEvent = {}
    }

    fun unbindKeyPressedEvents() {
        keyPressedMap.clear()
    }

    fun unbindKeyReleasedEvents() {
        keyReleasedMap.clear()
    }

    fun unbindMousePressedEvents() {
        mousePressedMap.clear()
    }

    fun unbindMouseReleasedEvents() {
        mouseReleasedMap.clear()
    }

    fun unbindKeyHoldEvents() {
        keyHoldMap.clear()
    }

    fun unbindMouseHoldEvents() {
        mouseHoldMap.clear()
    }

    fun onKeyPressed(key: Int) {
        keyPressedMap[key]?.invoke()

        if (keyHoldMap.containsKey(key)) {
            isKeyHold = true
            keyHold = key
        }
    }

    fun onKeyReleased(key: Int) {
        keyReleasedMap[key]?.invoke()
        isKeyHold = false
    }

    fun onMousePressed(mouseButton: Int) {
        mousePressedMap[mouseButton]?.invoke()

        if (mouseHoldMap.containsKey(mouseButton)) {
            isMouseHold = true
            mouseHold = mouseButton
        }
    }

    fun onMouseReleased(mouseButton: Int) {
        mouseReleasedMap[mouseButton]?.invoke()
        isMouseHold = false
    }

    fun onMouseScrollUp() {
        mouseScrollUpEvent.invoke()
    }

    fun onMouseScrollDown() {
        mouseScrollDownEvent.invoke()
    }

    fun onUpdate() {
        if (isKeyHold) {
            keyHoldMap[keyHold]?.invoke()
        }
        if (isMouseHold) {
            mouseHoldMap[mouseHold]?.invoke()
        }
    }

    override fun onDestroy() {
        unbindKeyPressedEvents()
        unbindKeyReleasedEvents()
        unbindKeyHoldEvents()
        unbindMousePressedEvents()
        unbindMouseReleasedEvents()
        unbindMouseHoldEvents()
        unbindMouseScrollDownEvent()
        unbindMouseScrollUpEvent()
    }

}
