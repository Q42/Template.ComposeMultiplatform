package nl.q42.template.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import nl.q42.template.core.actionresult.data.handleAction
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.navigation.viewmodel.RouteNavigator
import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.core.ui.presentation.ViewStateString
import nl.q42.template.core.ui.presentation.dialog.DialogData
import nl.q42.template.core.ui.presentation.dialog.DialogPresenter
import nl.q42.template.domain.main.usecase.FetchUserUseCase
import nl.q42.template.domain.main.usecase.GetUserFlowUseCase
import nl.q42.template.feature.home.resources.Res
import nl.q42.template.feature.home.resources.emailTitle
import kotlin.random.Random

class HomeViewModel(
    private val fetchUserUseCase: FetchUserUseCase,
    private val getUserFlowUseCase: GetUserFlowUseCase,
    private val snackbarManager: SnackbarManager,
    private val dialogPresenter: DialogPresenter,
    private val navigator: RouteNavigator,
) : ViewModel(), DialogPresenter by dialogPresenter, RouteNavigator by navigator {

    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    init {
        startObservingUserChanges()
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

    fun onOpenSecondScreenClicked() {
        navigateTo(Destination.HomeSecond(title = "Hello world!"))
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

    fun onShowExampleModalClicked() {
        navigateTo(Destination.HomeModalExampleGraph)
    }

    private fun fetchUser() {
        viewModelScope.launch {

            _uiState.value = HomeViewState.Loading

            handleAction(
                action = fetchUserUseCase(),
                onError = { _uiState.value = HomeViewState.Error },
                onSuccess = { user ->

                },
            )
        }
    }

    private fun startObservingUserChanges() {
        getUserFlowUseCase().filterNotNull().onEach { user ->
            println(user.email.value)
            _uiState.value = HomeViewState.Content(
                userEmailTitle = ViewStateString.Res(Res.string.emailTitle, user.email.value),
            )
        }.launchIn(viewModelScope)
    }
}
