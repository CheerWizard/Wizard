package application.graphics.core.mesh

import application.core.ecs.Component

class MeshComponent(
    var mesh: Mesh
) : Component {

    override var isUpdated: Boolean = true

    var usesCullFace = true

    companion object {
        const val ID: Short = 3
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
    }

}