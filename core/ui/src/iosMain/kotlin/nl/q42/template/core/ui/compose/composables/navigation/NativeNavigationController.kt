package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.cinterop.ExperimentalForeignApi
import nl.q42.template.core.ui.util.isIosAtLeast
import platform.Foundation.NSNumber
import platform.Foundation.valueForKey
import platform.UIKit.UIAction
import platform.UIKit.UIBarButtonItem
import platform.UIKit.UIImage
import platform.UIKit.UINavigationController
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIViewController
import platform.UIKit.accessibilityLabel
import platform.UIKit.navigationItem
import platform.objc.sel_registerName

/**
 * Root view controller on iOS 27.1+, around the [NativeTabBarController]. Never pushes anything, navigation stays
 * in Compose. It only exists for the vertical bar (the side rail on iPhone Duo): the system only moves the bar items
 * of a UINavigationController into it, a standalone UINavigationBar stays at the top.
 *
 * While there is a vertical bar, the navigation bar is visible and holds the back button, see [showBackButton].
 * Without one, the navigation bar is hidden: a horizontal bar would push the Compose content down, so the back
 * button is drawn in the Compose top bar instead.
 */
@OptIn(ExperimentalForeignApi::class)
internal class NativeNavigationController(
    root: UIViewController,
) : UINavigationController(rootViewController = root) {

    /** Whether the system places bars in a vertical bar. Compose state, so the back button can follow it. */
    var hasVerticalBar: Boolean by mutableStateOf(false)
        private set

    /** Back buttons of the composed screens, the last one is shown. During a transition two screens are composed. */
    private val backButtons = mutableListOf<Pair<Any, UIBarButtonItem>>()

    init {
        setNavigationBarHidden(true, animated = false)
    }

    // Every change of the vertical bar edge (folding, rotation, multitasking) also changes the layout
    override fun viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        val verticalBar = verticalBarEdge() != VERTICAL_BAR_EDGE_UNSPECIFIED
        if (verticalBar != hasVerticalBar) hasVerticalBar = verticalBar
        // Bar items only move into the vertical bar while the navigation bar is visible
        if (navigationBarHidden == verticalBar) setNavigationBarHidden(!verticalBar, animated = false)
    }

    /** Shows a back button in the vertical bar until [hideBackButton] is called with the same [owner]. */
    fun showBackButton(owner: Any, contentDescription: String, isDark: Boolean, onClick: () -> Unit) {
        val action = UIAction.actionWithTitle(
            title = contentDescription,
            image = UIImage.systemImageNamed("chevron.backward"),
            identifier = null,
        ) { onClick() }
        val item = UIBarButtonItem(primaryAction = action).apply { accessibilityLabel = contentDescription }
        backButtons.removeAll { it.first == owner }
        backButtons += owner to item
        // Follow the app theme rather than the system, in case the app overrides dark mode
        navigationBar.overrideUserInterfaceStyle =
            if (isDark) UIUserInterfaceStyle.UIUserInterfaceStyleDark else UIUserInterfaceStyle.UIUserInterfaceStyleLight
        updateBackButton()
    }

    fun hideBackButton(owner: Any) {
        backButtons.removeAll { it.first == owner }
        updateBackButton()
    }

    private fun updateBackButton() {
        val item = backButtons.lastOrNull()?.second
        val navigationItem = topViewController?.navigationItem ?: return
        if (navigationItem.leftBarButtonItem != item) navigationItem.setLeftBarButtonItem(item, animated = true)
    }

    /** `UITraitCollection.verticalBarEdge` (iOS 27.1) is not in the Kotlin/Native UIKit bindings yet, so read it by key. */
    private fun verticalBarEdge(): Long {
        val traits = traitCollection
        if (!traits.respondsToSelector(sel_registerName("verticalBarEdge"))) return VERTICAL_BAR_EDGE_UNSPECIFIED
        return (traits.valueForKey("verticalBarEdge") as? NSNumber)?.longValue ?: VERTICAL_BAR_EDGE_UNSPECIFIED
    }
}

/** `UIVerticalBarEdgeUnspecified`: the system doesn't use a vertical bar here */
private const val VERTICAL_BAR_EDGE_UNSPECIFIED = 0L

/** The vertical bar was introduced in iOS 27.1 */
private val isVerticalBarSupported: Boolean by lazy { isIosAtLeast(27, 1) }

/** Wraps the root view controller in a [NativeNavigationController] on iOS 27.1+, so the back button can move into the vertical bar. */
fun UIViewController.withNativeNavigationController(): UIViewController =
    if (isVerticalBarSupported) NativeNavigationController(this) else this
