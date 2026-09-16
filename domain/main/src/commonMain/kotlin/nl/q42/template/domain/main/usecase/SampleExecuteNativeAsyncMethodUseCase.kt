package nl.q42.template.domain.main.usecase

import nl.q42.template.interop.SampleNativeDependency

class SampleExecuteNativeAsyncMethodUseCase(
    private val sampleNativeDependency: SampleNativeDependency
) {
    suspend operator fun invoke() {
        println(sampleNativeDependency.executeNativeAsyncMethod())
    }
}