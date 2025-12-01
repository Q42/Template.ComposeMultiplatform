package nl.q42.template.domain.main.repo

interface AppSettingsRepository {
    suspend fun setOnboardingCompleted()
    suspend fun resetOnboardingCompleted()
    suspend fun isOnboardingCompleted(): Boolean
}
