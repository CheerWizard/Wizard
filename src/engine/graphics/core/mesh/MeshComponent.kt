package engine.graphics.core.mesh

import engine.core.ecs.Component

class MeshComponent(
    var mesh: Mesh,
    var cullFaceEnabled: Boolean = false
) : Component {

    companion object {
        const val ID: Short = 3
    }

    override fun getId(): Short = ID

    override fun onDestroy() {
        mesh.onDestroy()
    }

}