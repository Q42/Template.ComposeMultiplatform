package nl.q42.template.domain.main.repo

import kotlinx.coroutines.flow.Flow
import nl.q42.template.core.actionresult.model.ApiResult
import nl.q42.template.domain.main.model.SampleUser

interface SampleUserRepository {
    suspend fun fetchUser(): ApiResult<Unit>
    fun getUserFlow(): Flow<SampleUser?>
}
