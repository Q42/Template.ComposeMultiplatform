package nl.q42.template.feature.samplehome.presentation

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import nl.q42.template.core.actionresult.model.ActionResult
import nl.q42.template.core.actionresult.model.ApiError
import nl.q42.template.core.actionresult.model.ApiResult
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.testing.FakeDialogPresenter
import nl.q42.template.core.testing.FakeNavigator
import nl.q42.template.core.testing.FakeSampleUserRepository
import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.core.ui.presentation.ViewStateString
import nl.q42.template.domain.main.model.SampleUser
import nl.q42.template.domain.main.model.SampleUserName
import nl.q42.template.domain.main.usecase.SampleFetchUserUseCase
import nl.q42.template.domain.main.usecase.SampleGetPlatformUserGreetingFlowUseCase
import nl.q42.template.domain.main.usecase.SampleGetUserFlowUseCase
import nl.q42.template.feature.samplehome.resources.Res
import nl.q42.template.feature.samplehome.resources.title_user_name
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SampleHomeViewModelTest {

    // The user flow the ViewModel observes. Emissions are pushed from the tests, so state changes
    // happen at points the test controls.
    private val userFlow = MutableStateFlow<SampleUser?>(null)
    private var fetchUserResult: ApiResult<Unit> = ActionResult.Success(Unit)

    // Also handed to the use cases, so their work runs on the test scheduler instead of
    // Dispatchers.Default and state emissions arrive in a deterministic order.
    private val testDispatcher = StandardTestDispatcher()

    private val navigator = FakeNavigator()

    @BeforeTest
    fun setUp() {
        // viewModelScope and SnackbarManager's MainScope both dispatch on Main
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState starts as Loading`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(SampleHomeViewState.Loading, awaitItem())
        }
    }

    @Test
    fun `uiState emits Content with the user name once a user is available`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(SampleHomeViewState.Loading, awaitItem())
            // Let the initial fetch settle before pushing a user, so its Loading write (still
            // in flight from init) can't land after Content and revert the screen back to Loading.
            runCurrent()

            userFlow.value = SampleUser(SampleUserName("Ada"))
            runCurrent()

            assertEquals(
                SampleHomeViewState.Content(
                    userEmailTitle = ViewStateString.Res(Res.string.title_user_name, "Ada")
                ),
                awaitItem()
            )
        }
    }

    @Test
    fun `uiState emits Error when fetching the user fails`() = runTest(testDispatcher) {
        fetchUserResult = ActionResult.Error(ApiError.NetworkError(Throwable("no connection")))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(SampleHomeViewState.Loading, awaitItem())
            runCurrent()

            assertEquals(SampleHomeViewState.Error, awaitItem())
        }
    }

    @Test
    fun `onLoadClicked returns to Loading and emits Content on success`() = runTest(testDispatcher) {
        fetchUserResult = ActionResult.Error(ApiError.NetworkError(Throwable("no connection")))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(SampleHomeViewState.Loading, awaitItem())
            runCurrent()

            assertEquals(SampleHomeViewState.Error, awaitItem())

            fetchUserResult = ActionResult.Success(Unit)
            viewModel.onLoadClicked()

            assertEquals(SampleHomeViewState.Loading, awaitItem())

            userFlow.value = SampleUser(SampleUserName("Ada"))
            runCurrent()

            assertEquals(
                SampleHomeViewState.Content(
                    userEmailTitle = ViewStateString.Res(Res.string.title_user_name, "Ada")
                ),
                awaitItem()
            )
        }
    }

    @Test
    fun `uiState emits Content again when the user changes`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(SampleHomeViewState.Loading, awaitItem())
            // Let the initial fetch settle before pushing a user, so its Loading write (still
            // in flight from init) can't land after Content and revert the screen back to Loading.
            runCurrent()

            userFlow.value = SampleUser(SampleUserName("Ada"))
            runCurrent()
            assertEquals(
                SampleHomeViewState.Content(ViewStateString.Res(Res.string.title_user_name, "Ada")),
                awaitItem()
            )

            userFlow.value = SampleUser(SampleUserName("Grace"))
            runCurrent()
            assertEquals(
                SampleHomeViewState.Content(ViewStateString.Res(Res.string.title_user_name, "Grace")),
                awaitItem()
            )
        }
    }

    @Test
    fun `onOpenSampleInteropScreenClicked navigates to SampleInterop`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.onOpenSampleInteropScreenClicked()

        assertEquals(listOf<Destination>(Destination.SampleInterop), navigator.destinations)
    }

    @Test
    fun `onOpenOnboardingClicked navigates to Onboarding`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.onOpenOnboardingClicked()

        assertEquals(listOf<Destination>(Destination.Onboarding), navigator.destinations)
    }

    /**
     * Creates the ViewModel without draining the test scheduler, so its `init` block (which starts
     * the first user fetch) only progresses once a test subscribes to [SampleHomeViewModel.uiState] and
     * begins awaiting items — otherwise the fetch would resolve before subscription and the
     * intermediate Loading emission would never be observed.
     */
    private fun TestScope.createViewModel(): SampleHomeViewModel {
        val userRepository = FakeSampleUserRepository(
            userFlow = userFlow,
            fetchUser = { fetchUserResult },
        )

        return SampleHomeViewModel(
            fetchUserUseCase = SampleFetchUserUseCase(userRepository, testDispatcher),
            getUserFlowUseCase = SampleGetUserFlowUseCase(userRepository, testDispatcher),
            getPlatformUserGreetingFlowUseCase = SampleGetPlatformUserGreetingFlowUseCase(userRepository),
            snackbarManager = SnackbarManager(),
            dialogPresenter = FakeDialogPresenter(),
            navigator = navigator,
        )
    }
}
