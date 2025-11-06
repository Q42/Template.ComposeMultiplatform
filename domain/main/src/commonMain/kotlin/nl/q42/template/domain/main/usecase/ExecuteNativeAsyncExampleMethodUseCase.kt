package nl.q42.template.domain.main.usecase

import nl.q42.template.interop.NativeDependencyExample

class ExecuteNativeAsyncExampleMethodUseCase(
    private val nativeDependencyExample: NativeDependencyExample
) {
    suspend operator fun invoke() {
        println(nativeDependencyExample.executeNativeAsyncMethod())
    }
}