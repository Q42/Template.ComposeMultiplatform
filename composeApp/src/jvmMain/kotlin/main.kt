import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import nl.q42.template.App
import nl.q42.template.data.main.di.mainDataModule
import nl.q42.template.feature.home.di.homeModule
import org.koin.core.context.startKoin

fun main() = application {
    Window(
        title = "TemplateComposeMultiplatform",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)
        startKoin {
            modules(homeModule, mainDataModule)
        }
        App()
    }
}
