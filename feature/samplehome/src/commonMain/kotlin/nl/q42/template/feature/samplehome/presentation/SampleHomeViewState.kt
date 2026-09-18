package nl.q42.template.feature.samplehome.presentation

import nl.q42.template.core.ui.presentation.ViewStateString

sealed interface SampleHomeViewState {
    data class Content(val userEmailTitle: ViewStateString) : SampleHomeViewState
    data object Loading : SampleHomeViewState
    data object Error : SampleHomeViewState
}
