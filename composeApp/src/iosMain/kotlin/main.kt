import androidx.compose.ui.window.ComposeUIViewController
import nl.q42.template.App
import nl.q42.template.di.createAppModules
import nl.q42.template.interop.configuration.IosAppConfiguration
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

    App()
}
