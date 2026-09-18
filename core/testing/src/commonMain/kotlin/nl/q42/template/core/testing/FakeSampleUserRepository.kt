package nl.q42.template.core.testing

import kotlinx.coroutines.flow.Flow
import nl.q42.template.core.actionresult.model.ActionResult
import nl.q42.template.core.actionresult.model.ApiResult
import nl.q42.template.domain.main.model.SampleUser
import nl.q42.template.domain.main.repo.SampleUserRepository

class FakeSampleUserRepository(
    private val userFlow: Flow<SampleUser?>,
    private val fetchUser: suspend () -> ApiResult<Unit> = { ActionResult.Success(Unit) },
) : SampleUserRepository {
    override suspend fun fetchUser(): ApiResult<Unit> = fetchUser.invoke()
    override fun getUserFlow(): Flow<SampleUser?> = userFlow
}
