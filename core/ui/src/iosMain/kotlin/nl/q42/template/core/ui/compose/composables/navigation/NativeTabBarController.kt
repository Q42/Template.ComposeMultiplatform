package nl.q42.template.core.ui.compose.composables.navigation

import kotlinx.cinterop.ExperimentalForeignApi
import nl.q42.template.core.ui.util.isLiquidGlassAvailable
import platform.UIKit.UIImage
import platform.UIKit.UITabBarController
import platform.UIKit.UITabBarControllerDelegateProtocol
import platform.UIKit.UITabBarItem
import platform.UIKit.UIViewAutoresizingFlexibleHeight
import platform.UIKit.UIViewAutoresizingFlexibleWidth
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.beginAppearanceTransition
import platform.UIKit.endAppearanceTransition
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.tabBarItem
import platform.darwin.NSObject

/**
 * Root view controller on iOS 26+: a real UITabBarController, with the Compose view controller on top of its
 * (empty) tabs. Only the bar of a UITabBarController follows the system placement: at the bottom, or on the side
 * of the screen on iPhone Duo. A standalone UITabBar always stays at the bottom.
 *
 * Navigation stays in Compose, [NativeTabBar] sets the tabs and the selected tab.
 * The Compose view controller never moves into a tab: Compose disposes everything when its view leaves the window.
 */
@OptIn(ExperimentalForeignApi::class)
internal class NativeTabBarController(
    private val content: UIViewController,
) : UITabBarController(nibName = null, bundle = null) {

    internal var onItemSelected: (index: Int) -> Unit = {}
    internal var onLayout: () -> Unit = {}

    private var shownItems: List<AppNavigationBarItem>? = null

    // UITabBarController.delegate is weak, so we hold on to it here
    private val tabBarDelegate = TabBarControllerDelegate { onItemSelected(it) }

    // Not in viewDidLoad: UITabBarController loads its view in its constructor, before content is set
    init {
        delegate = tabBarDelegate
        // Hidden until Compose shows a screen with tabs
        setTabBarHidden(true)

        addChildViewController(content)
        // The tab bar controller view doesn't do Auto Layout for its subviews
        content.view.setFrame(view.bounds)
        content.view.autoresizingMask = UIViewAutoresizingFlexibleWidth or UIViewAutoresizingFlexibleHeight
        view.addSubview(content.view)
        content.didMoveToParentViewController(this)
    }

    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        keepContentAboveTabs()
        onLayout()
    }

    /** Only recreates the tabs when they changed */
    internal fun updateItems(items: List<AppNavigationBarItem>) {
        if (items == shownItems) return
        shownItems = items
        setViewControllers(
            items.mapIndexed { index, item ->
                UIViewController(nibName = null, bundle = null).apply {
                    tabBarItem = UITabBarItem(
                        title = item.label,
                        image = UIImage.imageNamed(item.iosImageName),
                        tag = index.toLong(),
                    )
                }
            },
            animated = false,
        )
        keepContentAboveTabs()
    }

    /**
     * The tabs are empty, Compose draws the content: keeps the Compose view controller a visible child, right above
     * the tab views. The tab bar stays on top of Compose.
     */
    internal fun keepContentAboveTabs() {
        // setViewControllers removes every other child view controller, and tells it that it disappeared
        if (content.parentViewController != this) {
            addChildViewController(content)
            content.didMoveToParentViewController(this)
            if (content.view.window != null) {
                content.beginAppearanceTransition(isAppearing = true, animated = false)
                content.endAppearanceTransition()
            }
        }
        val tabView = selectedViewController?.view ?: return
        val tabContainer = generateSequence(tabView) { it.superview }.firstOrNull { it.superview == view } ?: return
        if (view.subviews.indexOf(content.view) != view.subviews.indexOf(tabContainer) + 1) {
            view.insertSubview(content.view, aboveSubview = tabContainer)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class TabBarControllerDelegate(
    private val onItemSelected: (Int) -> Unit,
) : NSObject(), UITabBarControllerDelegateProtocol {
    override fun tabBarController(tabBarController: UITabBarController, didSelectViewController: UIViewController) {
        // Selecting a tab swaps the (empty) tab views, which puts them on top of Compose
        (tabBarController as? NativeTabBarController)?.keepContentAboveTabs()
        onItemSelected(tabBarController.selectedIndex.toInt())
    }
}

/** Wraps the Compose view controller in a [NativeTabBarController] on iOS 26+, so [AppNavigationBar] gets Liquid Glass. */
fun UIViewController.withNativeTabBarController(): UIViewController =
    if (isLiquidGlassAvailable) NativeTabBarController(this) else this
