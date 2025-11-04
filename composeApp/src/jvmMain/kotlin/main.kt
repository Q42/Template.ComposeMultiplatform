import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import nl.q42.template.App
import nl.q42.template.di.appModules
import nl.q42.template.interop.JvmNativeDependencyExample
import nl.q42.template.interop.NativeDependencyExample
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main() = application {
    Window(
        title = "TemplateComposeMultiplatform",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)

        startKoin {
            modules(
                appModules,
                module {
                    single<NativeDependencyExample> { JvmNativeDependencyExample() }
                }
            )
        }

        App()
    }
}
