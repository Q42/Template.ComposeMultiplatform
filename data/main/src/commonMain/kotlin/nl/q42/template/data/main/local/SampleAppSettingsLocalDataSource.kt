package nl.q42.template.data.main.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.first

internal class SampleAppSettingsLocalDataSource(
    private val dataStore: DataStore<Preferences>
) {
    @Suppress("PrivatePropertyName") // Suppressed because it's a constant key
    private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                preferences[ONBOARDING_COMPLETED_KEY] = completed
            }
        }
    }

    suspend fun isOnboardingCompleted(): Boolean {
        val settings = dataStore.data.first()
        return settings[ONBOARDING_COMPLETED_KEY] ?: false
    }
}
