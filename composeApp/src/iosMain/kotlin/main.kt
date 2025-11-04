import androidx.compose.ui.window.ComposeUIViewController
import nl.q42.template.App
import nl.q42.template.di.appModules
import nl.q42.template.interop.NativeDependencyExample
import nl.q42.template.interop.configuration.IosAppConfiguration
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.UIKit.UIViewController

fun MainViewController(
    iosAppConfiguration: IosAppConfiguration
): UIViewController = ComposeUIViewController {

    startKoin {
        modules(
            appModules,
            module {
                single<NativeDependencyExample> { iosAppConfiguration.nativeDependencyExample }
            }
        )
    }

    App()
}
