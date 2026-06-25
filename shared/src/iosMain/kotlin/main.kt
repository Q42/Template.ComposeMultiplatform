import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import nl.q42.template.App
import nl.q42.template.core.ui.compose.LocalNativeViewFactory
import nl.q42.template.di.createAppModules
import nl.q42.template.interop.configuration.IosAppConfiguration
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

var isKoinInitialized: Boolean = false

fun MainViewController(
    iosAppConfiguration: IosAppConfiguration
): UIViewController = ComposeUIViewController {

    // Only initialize Koin if it hasn't been started yet
    // This prevents KoinApplicationAlreadyStartedException when UIViewController is recreated (e.g., dark/light mode changes)
    if (!isKoinInitialized) {
        startKoin {
            modules(
                createAppModules(iosAppConfiguration.nativeDependencyExample)
            )
        }
        isKoinInitialized = true
    }

    CompositionLocalProvider(LocalNativeViewFactory provides iosAppConfiguration.nativeViewFactory) {
        App()
    }
}
