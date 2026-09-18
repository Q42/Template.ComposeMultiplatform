package nl.q42.template.core.testing

import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.navigation.viewmodel.BackstackBehavior
import nl.q42.template.core.navigation.viewmodel.Navigator

class FakeNavigator : Navigator {
    val destinations = mutableListOf<Destination>()

    override val currentDestination: Destination? get() = destinations.lastOrNull()
    override val backstack: List<Destination> get() = destinations

    override fun navigateTo(destination: Destination, backstackBehavior: BackstackBehavior) {
        destinations += destination
    }

    override fun navigateBack() {
        destinations.removeLastOrNull()
    }

    override fun popToRoute(destination: Destination) {
        while (destinations.isNotEmpty() && destinations.last() != destination) {
            destinations.removeLastOrNull()
        }
    }
}
