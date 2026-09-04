package nl.q42.template.domain.main.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.q42.template.core.actionresult.model.ApiResult
import nl.q42.template.domain.main.repo.SampleUserRepository

// A UseCase models an action so the name should begin with a verb. For Flows, use: GetSomethingFlowUseCase
class SampleFetchUserUseCase(
    private val userRepository: SampleUserRepository
) {

    suspend operator fun invoke(): ApiResult<Unit> = withContext(Dispatchers.Default) {
        userRepository.fetchUser()
    }
}
