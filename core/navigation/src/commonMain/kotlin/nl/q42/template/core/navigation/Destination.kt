package nl.q42.template.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {

    /**
     * Main destination. If you add a bottom navigation component, make a graph per bottom tab.
     */
    @Serializable
    data object HomeGraph : Destination()

    @Serializable
    data object Home : Destination()

    @Serializable
    data object InteropExamples : Destination()

    @Serializable
    data object Onboarding : Destination()

    @Serializable
    data object HomeModalExampleGraph : Destination()

    @Serializable
    data object HomeModalExample : Destination()
}
