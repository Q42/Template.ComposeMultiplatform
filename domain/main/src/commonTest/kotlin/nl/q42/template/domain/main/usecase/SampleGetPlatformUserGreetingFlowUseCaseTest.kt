package nl.q42.template.domain.main.usecase

import app.cash.turbine.test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import nl.q42.template.core.testing.FakeSampleUserRepository
import nl.q42.template.domain.main.model.SampleUser
import nl.q42.template.domain.main.model.SampleUserName
import kotlin.test.Test
import kotlin.test.assertTrue

class SampleGetPlatformUserGreetingFlowUseCaseTest {

    private val userFlow = MutableStateFlow<SampleUser?>(null)
    private val useCase = SampleGetPlatformUserGreetingFlowUseCase(FakeSampleUserRepository(userFlow))

    // getPlatformGreeting's wording is platform-specific (see the android/iosMain actuals), so
    // assertions only check that the resolved name is embedded rather than matching the full string.
    @Test
    fun `greeting falls back to Guest when there is no user`() = runTest {
        useCase().test {
            assertTrue(awaitItem().contains("Guest"))
        }
    }

    @Test
    fun `greeting uses the user name once a user is available`() = runTest {
        useCase().test {
            assertTrue(awaitItem().contains("Guest"))

            userFlow.value = SampleUser(SampleUserName("Ada"))

            assertTrue(awaitItem().contains("Ada"))
        }
    }

    @Test
    fun `greeting updates again when the user changes`() = runTest {
        useCase().test {
            assertTrue(awaitItem().contains("Guest"))

            userFlow.value = SampleUser(SampleUserName("Ada"))
            assertTrue(awaitItem().contains("Ada"))

            userFlow.value = SampleUser(SampleUserName("Grace"))
            assertTrue(awaitItem().contains("Grace"))
        }
    }
}
