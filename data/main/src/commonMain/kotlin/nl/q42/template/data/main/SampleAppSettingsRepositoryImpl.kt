package nl.q42.template.data.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.first
import nl.q42.template.domain.main.repo.SampleAppSettingsRepository

class SampleAppSettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SampleAppSettingsRepository {
    @Suppress("PrivatePropertyName") // Suppressed because it's a constant key
    private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

    override suspend fun setOnboardingCompleted() {
        setOnboardingCompletedState(true)
    }

    override suspend fun resetOnboardingCompleted() {
        setOnboardingCompletedState(false)
    }

    private suspend fun setOnboardingCompletedState(completed: Boolean) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[ONBOARDING_COMPLETED_KEY] = completed
            }
        }
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        val settings = dataStore.data.first()
        return settings[ONBOARDING_COMPLETED_KEY] ?: false
    }
}
