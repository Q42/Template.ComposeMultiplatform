package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.LocalThemeIsDark
import nl.q42.template.core.ui.util.toUIColor
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGRectZero
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIColor
import platform.UIKit.UIEvent
import platform.UIKit.UIImage
import platform.UIKit.UITabBarController
import platform.UIKit.UITabBarControllerDelegateProtocol
import platform.UIKit.UITabBarItem
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import platform.UIKit.removeFromParentViewController
import platform.UIKit.tabBarItem
import platform.UIKit.willMoveToParentViewController
import platform.darwin.NSObject

/**
 * Spike: a real UITabBarController, embedded as a child view controller of the Compose view controller.
 * Its tabs have empty view controllers, only its tab bar is used.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
internal fun NativeTabBar(
    items: List<AppNavigationBarItem>,
    selectedIndex: Int,
    onItemSelected: (index: Int) -> Unit,
    onHeightChanged: (Dp) -> Unit,
) {
    val parent = LocalUIViewController.current
    val currentOnItemSelected by rememberUpdatedState(onItemSelected)
    val currentOnHeightChanged by rememberUpdatedState(onHeightChanged)

    val delegate = remember { TabBarControllerDelegate { currentOnItemSelected(it) } }
    val controller = remember {
        EmbeddedTabBarController { currentOnHeightChanged(it.dp) }.apply { this.delegate = delegate }
    }

    DisposableEffect(parent) {
        parent.addChildViewController(controller)
        val view = TabBarOnlyView(controller)
        view.translatesAutoresizingMaskIntoConstraints = false
        controller.view.backgroundColor = UIColor.clearColor
        controller.view.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(controller.view)
        parent.view.addSubview(view)
        NSLayoutConstraint.activateConstraints(
            listOf(
                controller.view.leadingAnchor.constraintEqualToAnchor(view.leadingAnchor),
                controller.view.trailingAnchor.constraintEqualToAnchor(view.trailingAnchor),
                controller.view.topAnchor.constraintEqualToAnchor(view.topAnchor),
                controller.view.bottomAnchor.constraintEqualToAnchor(view.bottomAnchor),
                view.leadingAnchor.constraintEqualToAnchor(parent.view.leadingAnchor),
                view.trailingAnchor.constraintEqualToAnchor(parent.view.trailingAnchor),
                view.topAnchor.constraintEqualToAnchor(parent.view.topAnchor),
                view.bottomAnchor.constraintEqualToAnchor(parent.view.bottomAnchor),
            )
        )
        controller.didMoveToParentViewController(parent)
        onDispose {
            controller.willMoveToParentViewController(null)
            view.removeFromSuperview()
            controller.removeFromParentViewController()
            currentOnHeightChanged(0.dp)
        }
    }

    val isDark = LocalThemeIsDark.current.value
    val accent = AppTheme.colors.accent
    SideEffect {
        controller.updateItems(items)
        controller.selectedIndex = selectedIndex.toULong()
        controller.overrideUserInterfaceStyle =
            if (isDark) UIUserInterfaceStyle.UIUserInterfaceStyleDark else UIUserInterfaceStyle.UIUserInterfaceStyleLight
        controller.tabBar.tintColor = accent.toUIColor()
    }
}

@OptIn(ExperimentalForeignApi::class)
private class EmbeddedTabBarController(
    private val onHeightChanged: (Double) -> Unit,
) : UITabBarController(nibName = null, bundle = null) {

    private var lastHeight = -1.0
    private var shownItems: List<AppNavigationBarItem>? = null

    fun updateItems(items: List<AppNavigationBarItem>) {
        if (items == shownItems) return
        shownItems = items
        setViewControllers(
            items.mapIndexed { index, item ->
                UIViewController(nibName = null, bundle = null).apply {
                    view.backgroundColor = UIColor.clearColor
                    tabBarItem = UITabBarItem(
                        title = item.label,
                        image = UIImage.systemImageNamed(item.iosSystemImageName),
                        tag = index.toLong(),
                    )
                }
            },
            animated = false,
        )
    }

    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        val height = tabBar.bounds.useContents { size.height }
        if (height != lastHeight) {
            lastHeight = height
            onHeightChanged(height)
        }
    }
}

/** Covers the whole screen, but only the tab bar takes touches, the rest go to Compose. */
@OptIn(ExperimentalForeignApi::class)
private class TabBarOnlyView(
    private val controller: UITabBarController,
) : UIView(frame = CGRectZero.readValue()) {
    init {
        backgroundColor = UIColor.clearColor
    }

    override fun hitTest(point: CValue<CGPoint>, withEvent: UIEvent?): UIView? {
        val hit = super.hitTest(point, withEvent) ?: return null
        return if (hit.isDescendantOfView(controller.tabBar)) hit else null
    }
}

private class TabBarControllerDelegate(
    private val onItemSelected: (Int) -> Unit,
) : NSObject(), UITabBarControllerDelegateProtocol {
    override fun tabBarController(tabBarController: UITabBarController, didSelectViewController: UIViewController) {
        onItemSelected(tabBarController.selectedIndex.toInt())
    }
}
