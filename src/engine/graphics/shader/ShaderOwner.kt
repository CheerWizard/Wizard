package engine.graphics.shader

import engine.core.Destroyable
import engine.core.collection.DestroyableList
import engine.core.tools.Environment
import engine.graphics.core.buffers.MeshBuffer
import engine.graphics.core.buffers.VertexArray
import engine.graphics.core.texture.TextureBuffer
import engine.graphics.shader.attributes.Attribute
import engine.graphics.shader.attributes.AttributeList
import engine.graphics.shader.uniforms.*

abstract class ShaderOwner(
    protected val vertexShader: Shader,
    protected val fragmentShader: Shader
) : Destroyable {

    abstract var id: Int

    protected val uniformLocations = HashMap<String, Int>()

    private var isCreated = false
    private var isPrepared = false
    private var isDestroyed = false

    abstract val vertexArray: VertexArray
    abstract val meshBuffer: MeshBuffer

    val textureBuffers: DestroyableList<TextureBuffer> = DestroyableList()

    private var textureSlotCounter : Int = 0

    open fun onCreate() {
        if (isCreated) return
        isCreated = true

        vertexShader.onCreate()
        fragmentShader.onCreate()
    }

    open fun onPrepare() {
        if (isPrepared) return
        isPrepared = true

        findAttributesLocations()
        prepareBuffers()
    }

    private fun findAttributesLocations() {
        val meshAttributes = meshBuffer.getAttributeList()

        for (attribute in meshAttributes) {
            findAttributeLocation(attribute)
        }
    }

    fun activateTextures() {
        for (textureBuffer in textureBuffers) {
            textureBuffer.bind()
            textureBuffer.activate()
        }
    }

    fun deactivateTextures() {
        for (textureBuffer in textureBuffers) {
            textureBuffer.unbind()
        }
    }

    private fun prepareBuffers() {
        vertexArray.bind()
        meshBuffer.prepare()
        vertexArray.unbind()
    }

    open fun onStart() {
        vertexArray.bind()
        meshBuffer.enableAttributes()
        activateTextures()
    }

    open fun onStop() {
        deactivateTextures()
        meshBuffer.disableAttributes()
        vertexArray.unbind()
    }

    override fun onDestroy() {
        if (isDestroyed) return
        isDestroyed = true

        fragmentShader.onDestroy()
        vertexShader.onDestroy()

        meshBuffer.onDestroy()

        textureBuffers.clear()

        uniformLocations.clear()
        samplerArr2DList.clear()
        sampler2DList.clear()
    }

    fun addVertexAttribute(attribute: Attribute) {
        meshBuffer.vertexBuffer.addAttribute(attribute.apply {
            type = Attribute.VERTEX_TYPE
        })
    }

    fun addInstanceAttribute(attribute: Attribute) {
        meshBuffer.instanceBuffer.addAttribute(attribute.apply {
            type = Attribute.INSTANCE_TYPE
        })
    }

    fun getVertexAttributes() : AttributeList = meshBuffer.vertexBuffer.attributes

    fun getInstanceAttributes() : AttributeList = meshBuffer.instanceBuffer.attributes

    abstract fun createTexture2D(fileName: String, slot: Int = 0)
    abstract fun createTextureCubeMap(fileName: String, slot: Int = 0)

    private val sampler2DList = ArrayList<UInteger>()
    private val samplerArr2DList = ArrayList<UIntegerArray>()

    fun createSampler2D(name: String): Int {
        if (textureSlotCounter > Environment.TEXTURE_SLOTS) {
            textureSlotCounter = 0
        }

        sampler2DList.add(
            UInteger(
                name = name,
                value = textureSlotCounter
            )
        )

        return textureSlotCounter++
    }

    fun createSampler2DArray(name: String, size: Int) : IntArray {
        if (textureSlotCounter > Environment.TEXTURE_SLOTS) {
            textureSlotCounter = 0
        }

        val textureSlots = IntArray(size)
        for (i in 0 until size) {
            textureSlots[i] = i
        }

        samplerArr2DList.add(
            UIntegerArray(
                name = name,
                data = textureSlots
            )
        )

        textureSlotCounter += size
        return textureSlots
    }

    fun prepareSamplers() {
        for (sampler in sampler2DList) {
            putUniformName(sampler.name)
            sampler.isUpdated = true
        }

        for (samplerArr in samplerArr2DList) {
            putUniformName(samplerArr.name)
            samplerArr.isUpdated = true
        }
    }

    fun updateSamplers() {
        for (sampler in sampler2DList) {
            updateUniform(sampler)
        }

        for (samplerArr in samplerArr2DList) {
            if (samplerArr.isUpdated) {
                samplerArr.isUpdated = false
                onUpdateUniform(samplerArr)
            }
        }
    }

    abstract fun putUniformName(uniformName: String)

    fun updateUniform(uniform: UMatrix4f) {
        if (uniform.isUpdated) {
            uniform.isUpdated = false
            onUpdateUniform(uniform)
        }
    }

    protected abstract fun onUpdateUniform(uniform: UMatrix4f)

    fun updateUniform(uniform: UVector4f) {
        if (uniform.isUpdated) {
            uniform.isUpdated = false
            onUpdateUniform(uniform)
        }
    }

    protected abstract fun onUpdateUniform(uniform: UVector4f)

    fun updateUniform(uniform: UVector3f) {
        if (uniform.isUpdated) {
            uniform.isUpdated = false
            onUpdateUniform(uniform)
        }
    }

    protected abstract fun onUpdateUniform(uniform: UVector3f)

    fun updateUniform(uniform: UVector2f) {
        if (uniform.isUpdated) {
            uniform.isUpdated = false
            onUpdateUniform(uniform)
        }
    }

    protected abstract fun onUpdateUniform(uniform: UVector2f)

    fun updateUniform(uniform: UFloat) {
        if (uniform.isUpdated) {
            uniform.isUpdated = false
            onUpdateUniform(uniform)
        }
    }

    protected abstract fun onUpdateUniform(uniform: UFloat)

    fun updateUniform(uniform: UInteger) {
        if (uniform.isUpdated) {
            uniform.isUpdated = false
            onUpdateUniform(uniform)
        }
    }

    protected abstract fun onUpdateUniform(uniform: UInteger)

    fun updateUniform(uniform: UIntegerArray) {
        if (uniform.isUpdated) {
            uniform.isUpdated = false
            onUpdateUniform(uniform)
        }
    }

    protected abstract fun onUpdateUniform(uniform: UIntegerArray)

    protected abstract fun onUpdateUniform(name: String, value: Int)

    abstract fun updateUniformTrue(uniformName: String)
    abstract fun updateUniformFalse(uniformName: String)

    protected abstract fun getFragmentShader(fileName: String): Shader
    protected abstract fun getVertexShader(fileName: String): Shader
    protected abstract fun findAttributeLocation(attribute: Attribute)

}