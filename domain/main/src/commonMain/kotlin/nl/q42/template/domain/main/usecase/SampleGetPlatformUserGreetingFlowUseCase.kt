package nl.q42.template.domain.main.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nl.q42.template.domain.main.repo.SampleUserRepository

class SampleGetPlatformUserGreetingFlowUseCase(
    private val userRepository: SampleUserRepository
) {
    operator fun invoke(): Flow<String> {
        return userRepository.getUserFlow().map {
            getPlatformGreeting(
                userName = it?.name?.value ?: "Guest"
            )
        }
    }
}

expect suspend fun getPlatformGreeting(userName: String): String