package nl.q42.template.domain.main.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import nl.q42.template.domain.main.model.User
import nl.q42.template.domain.main.repo.UserRepository

class GetUserFlowUseCase(
    private val userRepository: UserRepository,
    // Overridable so tests can supply a TestDispatcher and keep the work on the test scheduler
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    operator fun invoke(): Flow<User?> =
        userRepository
            .getUserFlow()
            .flowOn(dispatcher)
}
