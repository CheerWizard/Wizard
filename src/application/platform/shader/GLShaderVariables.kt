package application.platform.shader

object GLShaderVariables {

    private val variables = hashMapOf<String, Int>(
        "vec2" to 2,
        "vec3" to 3,
        "vec4" to 4
    )

    fun contains(token: String): Boolean = variables.containsKey(token)

    fun getCheckedVarSize(token: String): Int = variables[token] as Int

    fun getVarSize(token: String): Int? = variables[token]

}