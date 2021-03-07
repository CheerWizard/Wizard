package application.graphics.material

import application.core.collection.DestroyableList
import application.core.ecs.Component
import application.graphics.texture.Texture
import application.graphics.uniform.UColor4f

class MaterialComponent(
        val materialBuffers: DestroyableList<MaterialBuffer> = DestroyableList(),
        var parallax: Parallax? = null
) : Component {

    companion object {
        const val ID: Short = 4
    }

    override fun getId(): Short = ID

    fun addMaterialBuffer(materialBuffer: MaterialBuffer): Int {
        materialBuffers.add(materialBuffer.apply {
            textureBuffer.apply {
                slot = materialBuffers.size
            }
        })
        return materialBuffers.size - 1
    }

    fun removeMaterialBuffer(materialBufferId: Int) {
        materialBuffers.removeAt(materialBufferId)
    }

    fun getMaterialBuffer(materialBufferId: Int): MaterialBuffer = materialBuffers[materialBufferId]

    override fun onDestroy() {
        materialBuffers.clear()
    }

    fun prepareMaterialBuffers() {
        for (materialBuffer in materialBuffers) {
            materialBuffer.prepare()
        }
    }

    fun bindMaterialBuffers() {
        for (materialBuffer in materialBuffers) {
            materialBuffer.bind()
        }
    }

    fun unbindMaterialBuffers() {
        for (materialBuffer in materialBuffers) {
            materialBuffer.unbind()
        }
    }

    fun enableAttributes() {
        for (materialBuffer in materialBuffers) {
            materialBuffer.enableAttributes()
        }
    }

    fun disableAttributes() {
        for (materialBuffer in materialBuffers) {
            materialBuffer.disableAttributes()
        }
    }

}