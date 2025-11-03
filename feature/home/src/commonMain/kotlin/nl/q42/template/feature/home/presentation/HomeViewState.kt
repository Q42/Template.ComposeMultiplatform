package nl.q42.template.feature.home.presentation

import nl.q42.template.core.ui.presentation.ViewStateString

sealed interface HomeViewState {
    data class Content(val userEmailTitle: ViewStateString, val interopExampleText: String) : HomeViewState
    data object Loading : HomeViewState
    data object Error : HomeViewState
}
