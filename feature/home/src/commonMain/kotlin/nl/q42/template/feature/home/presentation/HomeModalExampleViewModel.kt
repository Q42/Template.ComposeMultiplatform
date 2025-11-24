package nl.q42.template.feature.home.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import nl.q42.template.core.navigation.viewmodel.RouteNavigator

class HomeModalExampleViewModel(
    private val navigator: RouteNavigator,
) : ViewModel(), RouteNavigator by navigator {
    fun onScreenResumed() {
    }

    fun onCloseClicked() {
        navigateUp()
    }
}
