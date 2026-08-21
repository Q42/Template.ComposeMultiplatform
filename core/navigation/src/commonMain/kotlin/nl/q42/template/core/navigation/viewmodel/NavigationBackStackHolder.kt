package nl.q42.template.core.navigation.viewmodel

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Holds the currently active [NavBackStack] so that long-lived [Navigator] instances
 * (which may outlive an Activity recreation) always operate on the back stack that is
 * currently composed in [NavigationRoot], rather than a stale reference.
 */
class NavigationBackStackHolder {
    private val backStackUpdates = MutableSharedFlow<NavBackStack<NavKey>>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun update(backStack: NavBackStack<NavKey>) {
        backStackUpdates.tryEmit(backStack)
    }

    fun currentOr(initialBackStack: NavBackStack<NavKey>): NavBackStack<NavKey> {
        return backStackUpdates.replayCache.lastOrNull() ?: initialBackStack
    }
}