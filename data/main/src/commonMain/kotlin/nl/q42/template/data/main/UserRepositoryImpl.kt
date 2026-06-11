package nl.q42.template.data.main

import kotlinx.coroutines.flow.Flow
import nl.q42.template.core.actionresult.getDataOrNull
import nl.q42.template.core.actionresult.map
import nl.q42.template.core.actionresult.model.ApiResult
import nl.q42.template.data.main.local.UserLocalDataSource
import nl.q42.template.data.main.remote.UserRemoteDataSource
import nl.q42.template.domain.main.model.User
import nl.q42.template.domain.main.repo.UserRepository

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
) : UserRepository {

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

    override fun getUserFlow(): Flow<User?> =
        userLocalDataSource.getUserFlow()
}
