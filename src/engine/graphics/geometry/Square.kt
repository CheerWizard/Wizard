package engine.graphics.geometry

import engine.core.Updatable
import engine.graphics.core.mesh.Mesh

class Square : Mesh(
    indices = Updatable(
        data = intArrayOf(
            0, 1, 2,
            2, 3, 0
        )
    ),
)
