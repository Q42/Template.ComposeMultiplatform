import androidx.compose.ui.window.ComposeUIViewController
import nl.q42.template.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
