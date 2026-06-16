package nl.q42.template.data.main.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import nl.q42.template.core.actionresult.map
import nl.q42.template.core.actionresult.model.ApiResult
import nl.q42.template.data.main.remote.api.UserApi
import nl.q42.template.data.main.remote.model.UserDTO
import nl.q42.template.data.main.remote.model.toUser
import nl.q42.template.data.main.remote.util.getActionResult
import nl.q42.template.domain.main.model.User

internal class UserRemoteDataSource(
    private val userApi: UserApi
) {

    suspend fun getUser(): ApiResult<User> = withContext(Dispatchers.IO) {
        getActionResult<UserDTO> {
            userApi.getUser()
        }.map(UserDTO::toUser)
    }
}
