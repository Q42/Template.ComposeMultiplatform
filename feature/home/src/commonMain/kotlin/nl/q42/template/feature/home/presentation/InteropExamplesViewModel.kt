package nl.q42.template.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nl.q42.template.core.navigation.viewmodel.RouteNavigator
import nl.q42.template.domain.main.usecase.ExecuteNativeAsyncExampleMethodUseCase
import nl.q42.template.domain.main.usecase.ExecuteNativeExampleMethodUseCase

class InteropExamplesViewModel(
    private val executeNativeExampleMethodUseCase: ExecuteNativeExampleMethodUseCase,
    private val executeNativeAsyncExampleMethodUseCase: ExecuteNativeAsyncExampleMethodUseCase,
    private val navigator: RouteNavigator,
) : ViewModel(), RouteNavigator by navigator {

    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    fun onExecuteNativeExampleMethodClicked() {
        executeNativeExampleMethodUseCase.invoke()
    }

    fun onExecuteNativeAsyncExampleMethodClicked() {
        viewModelScope.launch {
            executeNativeAsyncExampleMethodUseCase.invoke()
        }
    }
}