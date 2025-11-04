package nl.q42.template.core.ui.compose

import androidx.compose.runtime.staticCompositionLocalOf
import nl.q42.template.interop.configuration.NativeViewFactory

val LocalNativeViewFactory = staticCompositionLocalOf<NativeViewFactory> {
    error("No NativeViewFactory provided")
}

