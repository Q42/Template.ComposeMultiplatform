package nl.q42.template.data.main.remote

import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import nl.q42.template.core.actionresult.domain.ActionResult
import nl.q42.template.data.main.local.model.UserEntity

internal class UserRemoteDataSource() {

    suspend fun getUser(): ActionResult<UserEntity> = withContext(Dispatchers.IO) {

        // This is currently a dummy call, we don't have a network layer yet
        delay(1000)

        val apiActionResult = ActionResult.Success(
            data = UserEntity(
                email = "test@user.com"
            )
        )

        when (apiActionResult) {
            is ActionResult.Success -> {
                apiActionResult
            }

            is ActionResult.Error -> {
                Logger.e(apiActionResult.throwable) { "getUser failed" }
                apiActionResult
            }
        }
    }
}
