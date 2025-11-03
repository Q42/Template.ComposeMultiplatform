package nl.q42.template.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    object Home : Destination()

    @Serializable
    object Onboarding : Destination()
}
