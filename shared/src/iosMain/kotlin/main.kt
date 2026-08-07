import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import nl.q42.template.App
import nl.q42.template.core.ui.compose.LocalNativeViewFactory
import nl.q42.template.di.createAppModules
import nl.q42.template.interop.configuration.IosAppConfiguration
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

var isKoinInitialized: Boolean = false

/**
 * Initializes Koin for the iOS application.
 * Call it exactly once in the iOS application lifecycle,
 * before any Koin components are used.
 */
fun initializeKoin(iosAppConfiguration: IosAppConfiguration) {
    if (!isKoinInitialized) {
        startKoin {
            modules(
                createAppModules(iosAppConfiguration.nativeDependencyExample)
            )
        }
        isKoinInitialized = true
    }
}

fun MainViewController(
    iosAppConfiguration: IosAppConfiguration
): UIViewController = ComposeUIViewController {
    CompositionLocalProvider(LocalNativeViewFactory provides iosAppConfiguration.nativeViewFactory) {
        App()
    }
}
