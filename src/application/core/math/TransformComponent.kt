package application.core.math

import application.core.ecs.Component

class TransformComponent(val transformMatrix4f: TransformMatrix4f = TransformMatrix4f()) : Component {

    override var isUpdated: Boolean = true

    companion object {
        const val ID: Short = 1
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

}