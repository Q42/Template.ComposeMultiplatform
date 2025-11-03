package nl.q42.template

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import nl.q42.template.core.navigation.Destination
import nl.q42.template.di.appModules
import nl.q42.template.interop.InteropProvider
import nl.q42.template.navigation.homeGraph
import nl.q42.template.navigation.onboardingDestinations
import nl.q42.template.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Preview
@Composable
internal fun App() = AppTheme {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.HomeGraph
    ) {
        homeGraph(
            navController = navController,
        )
        onboardingDestinations(navController)
    }
}
