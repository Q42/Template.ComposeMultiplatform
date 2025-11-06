import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import nl.q42.template.App
import nl.q42.template.core.ui.compose.LocalNativeViewFactory
import nl.q42.template.interop.NativeDependencyExample
import nl.q42.template.di.createAppModules
import nl.q42.template.interop.configuration.IosAppConfiguration
import nl.q42.template.interop.configuration.NativeViewFactory
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(
    iosAppConfiguration: IosAppConfiguration
): UIViewController = ComposeUIViewController {

    startKoin {
        modules(
            createAppModules(iosAppConfiguration.nativeDependencyExample)
        )
    }

    CompositionLocalProvider(LocalNativeViewFactory provides iosAppConfiguration.nativeViewFactory) {
        App()
    }
}
