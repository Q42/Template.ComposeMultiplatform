package nl.q42.template.core.navigation.viewmodel

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import nl.q42.template.core.navigation.Destination

interface Navigator {
    fun navigateTo(destination: Destination, backstackBehavior: BackstackBehavior = BackstackBehavior.Default)
    fun navigateBack()
    fun popToRoute(destination: Destination)
}

class NavigatorImpl(private val navigationBackStack: NavBackStack<NavKey>) : Navigator {
    override fun navigateTo(destination: Destination, backstackBehavior: BackstackBehavior) {
        navigationBackStack.add(destination)
    }

    override fun navigateBack() {
        navigationBackStack.removeLastOrNull()
    }

    override fun popToRoute(destination: Destination) {
        while (navigationBackStack.lastOrNull() != destination && navigationBackStack.isNotEmpty()) {
            navigationBackStack.removeLast()
        }
    }
}