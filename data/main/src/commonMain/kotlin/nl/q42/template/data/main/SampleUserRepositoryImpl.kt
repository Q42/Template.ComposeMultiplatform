package nl.q42.template.data.main

import kotlinx.coroutines.flow.Flow
import nl.q42.template.core.actionresult.getDataOrNull
import nl.q42.template.core.actionresult.map
import nl.q42.template.core.actionresult.model.ApiResult
import nl.q42.template.data.main.local.SampleUserLocalDataSource
import nl.q42.template.data.main.remote.SampleUserRemoteDataSource
import nl.q42.template.domain.main.model.SampleUser
import nl.q42.template.domain.main.repo.SampleUserRepository

internal class SampleUserRepositoryImpl(
    private val userRemoteDataSource: SampleUserRemoteDataSource,
    private val userLocalDataSource: SampleUserLocalDataSource,
) : SampleUserRepository {

    override suspend fun fetchUser(): ApiResult<Unit> {

        // get remotely
        val userActionResult = userRemoteDataSource.getUser()
        // store locally
        userActionResult.getDataOrNull()?.let { user ->
            userLocalDataSource.setUser(user)
        }
        // no need handle error case, we already do that in the remote data source

        // we send back unit, the user needs to be observed
        return userActionResult.map { }
    }

    override fun getUserFlow(): Flow<SampleUser?> =
        userLocalDataSource.getUserFlow()
}
