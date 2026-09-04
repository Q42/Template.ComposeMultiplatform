package nl.q42.template.domain.main.usecase

import nl.q42.template.interop.SampleNativeDependency

class SampleExecuteNativeMethodUseCase(
    private val sampleNativeDependency: SampleNativeDependency
) {
    operator fun invoke() = sampleNativeDependency.executeNativeMethod()
}