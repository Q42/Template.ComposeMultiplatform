package nl.q42.template.domain.main.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.q42.template.core.actionresult.model.ApiResult
import nl.q42.template.domain.main.repo.UserRepository

// A UseCase models an action so the name should begin with a verb. For Flows, use: GetSomethingFlowUseCase
class FetchUserUseCase(
    private val userRepository: UserRepository,
    // Overridable so tests can supply a TestDispatcher and keep the work on the test scheduler
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    suspend operator fun invoke(): ApiResult<Unit> = withContext(dispatcher) {
        userRepository.fetchUser()
    }
}
