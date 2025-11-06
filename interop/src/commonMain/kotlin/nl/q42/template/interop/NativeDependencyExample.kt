package nl.q42.template.interop

interface NativeDependencyExample {
    fun executeNativeMethod()

    suspend fun executeNativeAsyncMethod(): String
}