package application.graphics.material

import application.core.collection.DestroyableList
import application.core.ecs.Component
import application.graphics.texture.Texture
import application.graphics.uniform.UColor4f

class MaterialComponent(
    val textures: DestroyableList<Texture> = DestroyableList(),
    val color: UColor4f? = null,
    var parallax: Parallax? = null
) : Component {

    companion object {
        const val ID: Short = 4
    }

    override fun getId(): Short = ID

    fun addTexture(texture: Texture): Int {
        textures.add(texture.apply {
            slot = textures.size
        })
        return textures.size - 1
    }

    fun removeTexture(textureId: Int) {
        textures.removeAt(textureId)
    }

    fun getTexture(textureId: Int): Texture = textures[textureId]

    override fun onDestroy() {
        textures.clear()
    }

}