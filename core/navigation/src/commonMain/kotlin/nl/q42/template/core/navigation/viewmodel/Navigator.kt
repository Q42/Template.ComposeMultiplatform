package nl.q42.template.core.navigation.viewmodel

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import nl.q42.template.core.navigation.Route

interface Navigator {
    fun navigateTo(destination: Route, backstackBehavior: BackstackBehavior = BackstackBehavior.Default)
    fun navigateBack()
    fun popToRoute(destination: Route)
}

class NavigatorImpl(private val navigationBackStack: NavBackStack<NavKey>) : Navigator {
    override fun navigateTo(destination: Route, backstackBehavior: BackstackBehavior) {
        navigationBackStack.add(destination)
    }

    override fun navigateBack() {
        navigationBackStack.removeLastOrNull()
    }

    override fun popToRoute(destination: Route) {
        while (navigationBackStack.lastOrNull() != destination && navigationBackStack.isNotEmpty()) {
            navigationBackStack.removeLast()
        }
    }
}