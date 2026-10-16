package nl.q42.template.data.main

import kotlinx.coroutines.test.runTest
import nl.q42.template.core.testing.FakePreferencesDataStore
import nl.q42.template.data.main.local.SampleAppSettingsLocalDataSource
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SampleAppSettingsRepositoryImplTest {

    @Test
    fun `isOnboardingCompleted is false by default`() = runTest {
        val repository = createRepository()

        assertFalse(repository.isOnboardingCompleted())
    }

    @Test
    fun `setOnboardingCompleted marks onboarding as completed`() = runTest {
        val repository = createRepository()

        repository.setOnboardingCompleted()

        assertTrue(repository.isOnboardingCompleted())
    }

    @Test
    fun `resetOnboardingCompleted marks onboarding as not completed`() = runTest {
        val repository = createRepository()
        repository.setOnboardingCompleted()

        repository.resetOnboardingCompleted()

        assertFalse(repository.isOnboardingCompleted())
    }

    private fun createRepository() = SampleAppSettingsRepositoryImpl(
        appSettingsLocalDataSource = SampleAppSettingsLocalDataSource(FakePreferencesDataStore()),
    )
}
