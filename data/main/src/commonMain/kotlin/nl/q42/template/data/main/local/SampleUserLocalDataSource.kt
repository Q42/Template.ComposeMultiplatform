package nl.q42.template.data.main.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import nl.q42.template.domain.main.model.SampleUser

internal class SampleUserLocalDataSource constructor() {

    private val userFlow =
        MutableSharedFlow<SampleUser?>() // this is dummy code, replace it with your own local storage implementation.

    suspend fun setUser(user: SampleUser) {

        // usually you store in DataStore or DB here...

        userFlow.emit(user) // this is dummy code, replace it with your own local storage implementation.
    }

    fun getUserFlow(): Flow<SampleUser?> = userFlow
}
