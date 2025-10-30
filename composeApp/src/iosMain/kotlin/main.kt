import androidx.compose.ui.window.ComposeUIViewController
import nl.q42.template.App
import nl.q42.template.data.main.di.mainDataModule
import nl.q42.template.feature.home.di.homeModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    startKoin {
        modules(homeModule, mainDataModule)
    }
    App()
}
