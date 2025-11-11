package nl.q42.template.feature.home.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import nl.q42.template.core.navigation.viewmodel.RouteNavigator

class HomeModalExampleViewModel(
    private val navigator: RouteNavigator,
) : ViewModel(), RouteNavigator by navigator {

    private val _uiState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val uiState: StateFlow<HomeViewState> = _uiState.asStateFlow()

    fun onScreenResumed() {
    }

    fun onCloseClicked() {
        navigateUp()
    }
}
