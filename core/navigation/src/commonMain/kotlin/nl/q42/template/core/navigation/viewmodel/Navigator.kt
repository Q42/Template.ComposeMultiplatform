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
        when (backstackBehavior) {
            BackstackBehavior.Default -> {
                navigationBackStack.add(destination)
            }
            BackstackBehavior.Clear -> {
                navigationBackStack.clear()
                navigationBackStack.add(destination)
            }
            BackstackBehavior.RemoveCurrent -> {
                navigationBackStack.removeLastOrNull()
                navigationBackStack.add(destination)
            }
        }
    }

    override fun navigateBack() {
        if (navigationBackStack.size > 1) {
            navigationBackStack.removeLastOrNull()
        }
    }

    override fun popToRoute(destination: Destination) {
        val targetIndex = navigationBackStack.indexOf(destination)
        if (targetIndex == -1) {
            return
        }

        while (navigationBackStack.lastIndex > targetIndex) {
            navigationBackStack.removeAt(navigationBackStack.lastIndex)
        }
    }
}