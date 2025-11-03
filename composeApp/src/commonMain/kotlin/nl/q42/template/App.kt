package nl.q42.template

import HomeScreen
import OnboardingScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.utils.interop.InteropProvider
import nl.q42.template.data.main.di.mainDataModule
import nl.q42.template.feature.home.di.homeModule
import nl.q42.template.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Preview
@Composable
internal fun App() = AppTheme {

    val navController = rememberNavController()
    val onNavigate = { destination: Destination ->
        navController.navigate(destination)
    }
    val onBack: () -> Unit = { navController.popBackStack() }

    NavHost(navController = navController, startDestination = Destination.Home) {
        composable<Destination.Home> { HomeScreen(onNavigate, onBack) }
        composable<Destination.Onboarding> { OnboardingScreen(onNavigate, onBack) }
    }
}

fun initializeKoin(interopProvider: InteropProvider) {

    startKoin {
        modules(
            module {
                single<InteropProvider> { interopProvider }
            },
            homeModule,
            mainDataModule)
    }
}

