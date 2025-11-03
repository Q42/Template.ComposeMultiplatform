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
    // all parameters should be path parameters of a deeplink in HomeGraph.kt: composable<Destination.HomeSecond>(deeplinks = listOf(...))
    data class HomeSecond(val title: String) : Destination()

    @Serializable
    data object Onboarding : Destination()
}
