package nl.q42.template.data.main.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import nl.q42.template.core.actionresult.map
import nl.q42.template.core.actionresult.model.ApiResult
import nl.q42.template.data.main.remote.api.SampleUserApi
import nl.q42.template.data.main.remote.model.SampleUserDTO
import nl.q42.template.data.main.remote.model.toSampleUser
import nl.q42.template.data.main.remote.util.getActionResult
import nl.q42.template.domain.main.model.SampleUser

internal class SampleUserRemoteDataSource(
    private val userApi: SampleUserApi
) {

    suspend fun getUser(): ApiResult<SampleUser> = withContext(Dispatchers.IO) {
        getActionResult<SampleUserDTO> {
            userApi.getUser()
        }.map(SampleUserDTO::toSampleUser)
    }
}
