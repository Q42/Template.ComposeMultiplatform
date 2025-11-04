package nl.q42.template.interop

import kotlinx.coroutines.delay

// TODO: Implement any JVM-specific native functionality here
class JvmNativeDependencyExample: NativeDependencyExample {
    override fun executeNativeMethod() {
        println("Hello from JVM!")
    }

    override suspend fun executeNativeAsyncMethod(): String {
        delay(2000) // Simulate some asynchronous work
        return "Hello from JVM async!"
    }
}