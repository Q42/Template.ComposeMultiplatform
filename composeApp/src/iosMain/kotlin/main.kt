import androidx.compose.ui.window.ComposeUIViewController
import nl.q42.template.App
import nl.q42.template.di.appModules
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    App()
}
