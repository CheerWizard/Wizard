package engine.graphics.geometry

import engine.core.Updatable
import engine.graphics.core.mesh.Mesh

class Triangle : Mesh(
    indices = Updatable(
        data = intArrayOf(
            0, 1, 2
        )
    )
)