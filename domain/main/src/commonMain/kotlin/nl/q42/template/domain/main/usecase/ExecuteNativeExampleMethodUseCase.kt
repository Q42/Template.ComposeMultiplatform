package nl.q42.template.domain.main.usecase

import nl.q42.template.interop.NativeDependencyExample

class ExecuteNativeExampleMethodUseCase(
    private val nativeDependencyExample: NativeDependencyExample
) {
    operator fun invoke() = nativeDependencyExample.executeNativeMethod()
}