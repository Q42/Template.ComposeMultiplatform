package nl.q42.template.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import nl.q42.template.core.actionresult.handleAction
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.core.ui.presentation.ViewStateString
import nl.q42.template.core.ui.presentation.dialog.DialogData
import nl.q42.template.core.ui.presentation.dialog.DialogPresenter
import nl.q42.template.domain.main.usecase.FetchUserUseCase
import nl.q42.template.domain.main.usecase.GetPlatformUserGreetingFlowUseCase
import nl.q42.template.domain.main.usecase.GetUserFlowUseCase
import nl.q42.template.feature.home.resources.Res
import nl.q42.template.feature.home.resources.title_user_name
import kotlin.random.Random

class HomeViewModel(
    private val fetchUserUseCase: FetchUserUseCase,
    private val getUserFlowUseCase: GetUserFlowUseCase,
    private val getPlatformUserGreetingFlowUseCase: GetPlatformUserGreetingFlowUseCase,
    private val snackbarManager: SnackbarManager,
    private val dialogPresenter: DialogPresenter,
    private val navigator: Navigator,
) : ViewModel(), DialogPresenter by dialogPresenter, Navigator by navigator {

    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    init {
        startObservingUserChanges()
        startObservingPlatformUserGreeting()
        fetchUser()
    }

    override fun onDialogConfirmed(tag: Any) {
        dialogPresenter.onDialogConfirmed(tag)
        // take action based on dialog tag
        snackbarManager.showSnackbar(message = ViewStateString.Basic("Dialog confirmed with tag: $tag"))
    }

    fun onScreenResumed() {
    }

    fun onLoadClicked() {
        // A Loading state means a fetch is already running, so don't start a second one
        if (uiState.value is HomeViewState.Loading) return

        fetchUser()
    }

    fun onOpenInteropExamplesScreenClicked() {
        navigateTo(Destination.InteropExamples)
    }

    fun onOpenOnboardingClicked() {
        navigateTo(Destination.Onboarding)
    }

    fun onShowDummySnackBarClicked() {
        snackbarManager.showSnackbar(
            message = ViewStateString.Basic("A SnackBar message. Random: " + Random.nextInt() % 100),
            isError = false
        )
    }

    fun onShowDialogClicked() {
        dialogPresenter.showDialog(
            data = DialogData(
                title = ViewStateString.Basic("Dialog Title"),
                description = ViewStateString.Basic("This is a dialog message. It can be used to show more information or ask for confirmation."),
                tag = "userId 1337",
            )
        )
    }

    fun onLogToFirebaseClicked() {
        Logger.i("Test log 1")
        Logger.i("Test log 2")
        Logger.e("Test error log from HomeViewModel", Throwable("Test exception"))
    }

    private fun fetchUser() {
        viewModelScope.launch {

            _uiState.value = HomeViewState.Loading

            handleAction(
                action = fetchUserUseCase(),
                onError = { _uiState.value = HomeViewState.Error },
                onSuccess = { },
            )
        }
    }

    private fun startObservingUserChanges() {
        getUserFlowUseCase()
            .filterNotNull()
            .onEach { user ->
                _uiState.value = HomeViewState.Content(
                    userEmailTitle = ViewStateString.Res(Res.string.title_user_name, user.name.value),
                )
            }.launchIn(viewModelScope)
    }

    private fun startObservingPlatformUserGreeting() {
        getPlatformUserGreetingFlowUseCase()
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { greeting ->
                snackbarManager.showSnackbar(message = ViewStateString.Basic(greeting))
            }.launchIn(viewModelScope)
    }
}
