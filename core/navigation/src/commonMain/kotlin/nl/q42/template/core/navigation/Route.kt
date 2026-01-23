package nl.q42.template.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

//    @Serializable
//    data object HomeGraph : Destination()

    @Serializable
    data object Home : Route, NavKey

    @Serializable
    data object InteropExamples : Route, NavKey

    @Serializable
    data object Onboarding : Route
//
//    @Serializable
//    data object HomeModalExampleGraph : Destination()
//
//    @Serializable
//    data object HomeModalExample : Destination()

}