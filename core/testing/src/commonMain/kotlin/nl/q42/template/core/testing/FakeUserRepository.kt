package nl.q42.template.core.testing

import kotlinx.coroutines.flow.Flow
import nl.q42.template.core.actionresult.model.ActionResult
import nl.q42.template.core.actionresult.model.ApiResult
import nl.q42.template.domain.main.model.User
import nl.q42.template.domain.main.repo.UserRepository

class FakeUserRepository(
    private val userFlow: Flow<User?>,
    private val fetchUser: suspend () -> ApiResult<Unit> = { ActionResult.Success(Unit) },
) : UserRepository {
    override suspend fun fetchUser(): ApiResult<Unit> = fetchUser.invoke()
    override fun getUserFlow(): Flow<User?> = userFlow
}
