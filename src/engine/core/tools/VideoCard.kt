package engine.core.tools

abstract class VideoCard {
    abstract val renderer: String
    abstract val vendor: String
    abstract val version: String
    abstract val shaderLanguage: String
    abstract val extensions: String
    abstract val maxTextureSlots: Byte
}