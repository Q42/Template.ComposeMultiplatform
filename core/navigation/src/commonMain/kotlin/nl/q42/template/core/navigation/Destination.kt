package nl.q42.template.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination : NavKey {

    @Serializable
    data object Home : Destination, NavKey

    @Serializable
    data object SampleInterop : Destination, NavKey

    @Serializable
    data object Onboarding : Destination, NavKey
}