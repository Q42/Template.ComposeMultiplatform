package nl.q42.template.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.navigation.viewmodel.NavigationBackStackHolder
import nl.q42.template.core.navigation.viewmodel.NavigatorImpl
import nl.q42.template.core.ui.compose.composables.navigation.AppNavigationBar
import nl.q42.template.core.ui.compose.composables.navigation.LocalAppNavigationBarHeight
import nl.q42.template.core.ui.theme.AppTheme
import org.koin.compose.koinInject

@OptIn(InternalSerializationApi::class)
@Composable
fun NavigationRoot() {

    /* The rememberNavBackStack is automatically persisted across process death and configuration
    * changes. So it can be passed into ViewModels, which outlive the views.
    *
    * This means that our NavKeys (Routes) need to be serialized and deserialized to be able to save
    * and restore the back stack. On native Android, Nav 3 can use reflection to automatically
    * derive the required serializers for the NavKeys, but on KMP this is not possible because
    * reflection is a little more limited here. Therefore, we need to provide the serializers for
    * each NavKey manually, using the serializersModule.
    */
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Destination.Home::class, Destination.Home::class.serializer())
                    subclass(Destination.SampleInterop::class, Destination.SampleInterop::class.serializer())
                    subclass(Destination.Onboarding::class, Destination.Onboarding::class.serializer())
                }
            }
        },
        Destination.Home
    )

    val navigationBackStackHolder: NavigationBackStackHolder = koinInject()

    LaunchedEffect(backStack) {
        navigationBackStackHolder.update(backStack)
    }

    val navigator = remember(backStack) {
        NavigatorImpl(
            initialNavigationBackStack = backStack,
            navigationBackStackHolder = navigationBackStackHolder,
        )
    }

    /*
     * EntryDecorators:
     * rememberSavableStateHolderNavEntryDecorator: required to make sure the backstack is properly
     * persisted across config changes.
     *
     * rememberViewModelStoreNavEntryDecorator: required to make sure that viewModels are properly
     * scoped to the corresponding views and cleared when the view is removed from the backstack.
    */
    val selectedTab = BottomBarTab.entries.find { it.destination == backStack.lastOrNull() }
    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val visibleBottomBarHeight = if (selectedTab != null) bottomBarHeight else 0.dp

    Box(Modifier.fillMaxSize().background(AppTheme.colors.surface)) {
        // Screens draw behind the bottom bar, so their content scrolls behind the Liquid Glass.
        // ScaffoldWithAppBar adds the bar height as content padding.
        CompositionLocalProvider(LocalAppNavigationBarHeight provides visibleBottomBarHeight) {
            NavDisplay(
                // The bottom bar already covers the navigation bar insets, so screens should not add them again
                modifier = Modifier.consumeWindowInsets(PaddingValues(bottom = visibleBottomBarHeight)),
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                backStack = backStack,
                transitionSpec = transitionSpec(),
                popTransitionSpec = popTransitionSpec(),
                predictivePopTransitionSpec = predictivePopTransitionSpec(),
                entryProvider = { key ->
                    navEntryForKey(key = key, navigator = navigator)
                }
            )
        }

        if (selectedTab != null) {
            val tabs = BottomBarTab.entries
            AppNavigationBar(
                items = tabs.map { it.toNavigationBarItem() },
                selectedIndex = tabs.indexOf(selectedTab),
                onItemSelected = { index ->
                    if (tabs[index] != selectedTab) navigator.navigateToTab(tabs[index])
                },
                onHeightChanged = { bottomBarHeight = it },
            )
        }
    }
}

private fun transitionSpec(): AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
    slideInHorizontally { it } + fadeIn() togetherWith
            slideOutHorizontally { -it } + fadeOut()
}

private fun popTransitionSpec(): AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform = {
    slideInHorizontally { -it } + fadeIn() togetherWith
            slideOutHorizontally { it } + fadeOut()
}

private fun predictivePopTransitionSpec(): AnimatedContentTransitionScope<Scene<NavKey>>.(Int) -> ContentTransform = {
    slideInHorizontally { -it } + fadeIn() togetherWith
            slideOutHorizontally { it } + fadeOut()
}

