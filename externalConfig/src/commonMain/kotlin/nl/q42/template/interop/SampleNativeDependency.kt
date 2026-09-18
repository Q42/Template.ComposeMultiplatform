package nl.q42.template.interop

interface SampleNativeDependency {
    fun executeNativeMethod()

    suspend fun executeNativeAsyncMethod(): String
}