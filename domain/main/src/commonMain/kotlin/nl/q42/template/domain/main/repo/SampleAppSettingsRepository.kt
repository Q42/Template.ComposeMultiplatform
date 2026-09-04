package nl.q42.template.domain.main.repo

interface SampleAppSettingsRepository {
    suspend fun setOnboardingCompleted()
    suspend fun resetOnboardingCompleted()
    suspend fun isOnboardingCompleted(): Boolean
}
