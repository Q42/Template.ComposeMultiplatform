package nl.q42.template.interop

import kotlinx.coroutines.delay

class AndroidSampleNativeDependency: SampleNativeDependency {
    override fun executeNativeMethod() {
        println("Hello from Android!")
    }

    override suspend fun executeNativeAsyncMethod(): String {
        delay(2000) // Simulate some asynchronous work
        return "Hello from Android async!"
    }
}