package engine.core.tools

object FpsTicker {

    var maxFps: Long = 60

    private var deltaTime: Long = 0

    fun getDeltaTimeSeconds() : Float = deltaTime.toFloat() / 1000

    fun getDeltaTimeMillis() : Long = deltaTime

    fun deltaTimeOf(action : () -> Unit) {
        val startFrameTime = System.currentTimeMillis()

        action.invoke()

        deltaTime = System.currentTimeMillis() - startFrameTime

        val maxDeltaTime = 1000 / maxFps
        if (deltaTime <= maxDeltaTime) {
            deltaTime = maxDeltaTime
            Thread.sleep(maxDeltaTime)
        }

        println("Frame update time = $deltaTime ms!")
    }

}