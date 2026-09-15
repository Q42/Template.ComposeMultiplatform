package nl.q42.template.feature.samplehome.presentation

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
import nl.q42.template.domain.main.usecase.SampleFetchUserUseCase
import nl.q42.template.domain.main.usecase.SampleGetPlatformUserGreetingFlowUseCase
import nl.q42.template.domain.main.usecase.SampleGetUserFlowUseCase
import nl.q42.template.feature.samplehome.resources.Res
import nl.q42.template.feature.samplehome.resources.title_user_name
import org.koin.core.annotation.InjectedParam
import kotlin.random.Random

class SampleHomeViewModel(
    private val fetchUserUseCase: SampleFetchUserUseCase,
    private val getUserFlowUseCase: SampleGetUserFlowUseCase,
    private val getPlatformUserGreetingFlowUseCase: SampleGetPlatformUserGreetingFlowUseCase,
    private val snackbarManager: SnackbarManager,
    private val dialogPresenter: DialogPresenter,
    @InjectedParam private val navigator: Navigator,
) : ViewModel(), DialogPresenter by dialogPresenter, Navigator by navigator {

    private val _uiState = MutableStateFlow<SampleHomeViewState>(SampleHomeViewState.Loading)
    val uiState: StateFlow<SampleHomeViewState> = _uiState.asStateFlow()

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
        fetchUser()
    }

    fun onOpenSampleInteropScreenClicked() {
        navigateTo(Destination.SampleInterop)
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
        Logger.e("Test error log from SampleHomeViewModel", Throwable("Test exception"))
    }

    private fun fetchUser() {
        viewModelScope.launch {

            _uiState.value = SampleHomeViewState.Loading

            handleAction(
                action = fetchUserUseCase(),
                onError = { _uiState.value = SampleHomeViewState.Error },
                onSuccess = { },
            )
        }
    }

    private fun startObservingUserChanges() {
        getUserFlowUseCase()
            .filterNotNull()
            .onEach { user ->
                _uiState.value = SampleHomeViewState.Content(
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
