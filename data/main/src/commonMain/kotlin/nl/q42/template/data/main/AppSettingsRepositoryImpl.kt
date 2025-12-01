package nl.q42.template.data.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.first
import nl.q42.template.domain.main.repo.AppSettingsRepository

class AppSettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : AppSettingsRepository {
    private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

    override suspend fun setOnboardingCompleted() {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[ONBOARDING_COMPLETED_KEY] = true
            }
        }
    }

    override suspend fun resetOnboardingCompleted() {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[ONBOARDING_COMPLETED_KEY] = false
            }
        }
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        val settings = dataStore.data.first()
        return settings[ONBOARDING_COMPLETED_KEY] ?: false
    }
}
