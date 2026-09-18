package nl.q42.template.data.main

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AppSettingsRepositoryImplTest {

    @Test
    fun `isOnboardingCompleted is false by default`() = runTest {
        val repository = AppSettingsRepositoryImpl(FakePreferencesDataStore())

        assertFalse(repository.isOnboardingCompleted())
    }

    @Test
    fun `setOnboardingCompleted marks onboarding as completed`() = runTest {
        val repository = AppSettingsRepositoryImpl(FakePreferencesDataStore())

        repository.setOnboardingCompleted()

        assertTrue(repository.isOnboardingCompleted())
    }

    @Test
    fun `resetOnboardingCompleted marks onboarding as not completed`() = runTest {
        val repository = AppSettingsRepositoryImpl(FakePreferencesDataStore())
        repository.setOnboardingCompleted()

        repository.resetOnboardingCompleted()

        assertFalse(repository.isOnboardingCompleted())
    }
}
