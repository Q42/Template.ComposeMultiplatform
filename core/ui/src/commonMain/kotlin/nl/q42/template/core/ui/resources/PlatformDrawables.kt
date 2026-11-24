package nl.q42.template.core.ui.resources

import org.jetbrains.compose.resources.DrawableResource

// Compose resources can't be qualified per platform, so these pick a drawable per platform instead.

/** Back navigation icon: an arrow on Android, a chevron on iOS. */
expect val Res.drawable.navigation_back_24: DrawableResource
