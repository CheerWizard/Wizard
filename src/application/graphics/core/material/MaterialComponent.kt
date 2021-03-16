package application.graphics.core.material

import application.core.ecs.Component

class MaterialComponent(
    var material: Material
) : Component {

    override var isUpdated: Boolean = true

    companion object {
        const val ID: Short = 4
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

}