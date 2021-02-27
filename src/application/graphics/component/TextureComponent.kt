package application.graphics.component

import application.core.collection.DestroyableList
import application.core.ecs.Component
import application.graphics.texture.Texture

inline class TextureComponent(val textures: DestroyableList<Texture> = DestroyableList()) : Component {

    companion object {
        const val ID: Short = 2
    }

    override fun getId(): Short = ID

    fun addTexture(texture: Texture): Int {
        textures.add(texture.apply {
            uniformSamplerValue = textures.size
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