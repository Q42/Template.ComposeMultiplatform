package nl.q42.template.data.main

import kotlinx.coroutines.test.runTest
import nl.q42.template.core.testing.FakePreferencesDataStore
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SampleAppSettingsRepositoryImplTest {

    @Test
    fun `isOnboardingCompleted is false by default`() = runTest {
        val repository = SampleAppSettingsRepositoryImpl(FakePreferencesDataStore())

        assertFalse(repository.isOnboardingCompleted())
    }

    @Test
    fun `setOnboardingCompleted marks onboarding as completed`() = runTest {
        val repository = SampleAppSettingsRepositoryImpl(FakePreferencesDataStore())

        repository.setOnboardingCompleted()

        assertTrue(repository.isOnboardingCompleted())
    }

    @Test
    fun `resetOnboardingCompleted marks onboarding as not completed`() = runTest {
        val repository = SampleAppSettingsRepositoryImpl(FakePreferencesDataStore())
        repository.setOnboardingCompleted()

        repository.resetOnboardingCompleted()

        assertFalse(repository.isOnboardingCompleted())
    }
}
