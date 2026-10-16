package nl.q42.template.data.main

import nl.q42.template.data.main.local.SampleAppSettingsLocalDataSource
import nl.q42.template.domain.main.repo.SampleAppSettingsRepository

internal class SampleAppSettingsRepositoryImpl(
    private val appSettingsLocalDataSource: SampleAppSettingsLocalDataSource,
) : SampleAppSettingsRepository {

    override suspend fun setOnboardingCompleted() {
        appSettingsLocalDataSource.setOnboardingCompleted(true)
    }

    override suspend fun resetOnboardingCompleted() {
        appSettingsLocalDataSource.setOnboardingCompleted(false)
    }

    override suspend fun isOnboardingCompleted(): Boolean =
        appSettingsLocalDataSource.isOnboardingCompleted()
}
