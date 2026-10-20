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
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.LocalThemeIsDark
import nl.q42.template.core.ui.util.toUIColor
import platform.CoreGraphics.CGRectZero
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIImage
import platform.UIKit.UITabBar
import platform.UIKit.UITabBarDelegateProtocol
import platform.UIKit.UITabBarItem
import platform.UIKit.UIUserInterfaceStyle
import platform.darwin.NSObject

/**
 * Native UITabBar, added on top of the Compose view. UIKit takes care of the Liquid Glass look,
 * accessibility (VoiceOver, Large Content Viewer for large Dynamic Type sizes), safe areas and rotation.
 *
 * Inspired by Calf's AdaptiveNavigationBar, but reports its height from layoutSubviews instead of polling.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
internal fun NativeTabBar(
    items: List<AppNavigationBarItem>,
    selectedIndex: Int,
    onItemSelected: (index: Int) -> Unit,
    onHeightChanged: (Dp) -> Unit,
) {
    val rootView = LocalUIViewController.current.view
    val currentOnItemSelected by rememberUpdatedState(onItemSelected)
    val currentOnHeightChanged by rememberUpdatedState(onHeightChanged)

    // UITabBar.delegate is weak, so we hold on to it here
    val delegate = remember { TabBarDelegate { currentOnItemSelected(it) } }
    val tabBar = remember {
        MeasuredTabBar { currentOnHeightChanged(it.dp) }.apply {
            translatesAutoresizingMaskIntoConstraints = false
            this.delegate = delegate
        }
    }

    DisposableEffect(rootView) {
        rootView.addSubview(tabBar)
        // Pinned to the bottom of the view (not the safe area), UITabBar adds the safe area to its own height
        NSLayoutConstraint.activateConstraints(
            listOf(
                tabBar.leadingAnchor.constraintEqualToAnchor(rootView.leadingAnchor),
                tabBar.trailingAnchor.constraintEqualToAnchor(rootView.trailingAnchor),
                tabBar.bottomAnchor.constraintEqualToAnchor(rootView.bottomAnchor),
            )
        )
        onDispose {
            tabBar.removeFromSuperview()
            currentOnHeightChanged(0.dp)
        }
    }

    val isDark = LocalThemeIsDark.current.value
    val accent = AppTheme.colors.accent
    SideEffect {
        tabBar.updateItems(items)
        tabBar.selectedItem = tabBar.items?.getOrNull(selectedIndex) as? UITabBarItem
        // Follow the app theme rather than the system, in case the app overrides dark mode
        tabBar.overrideUserInterfaceStyle =
            if (isDark) UIUserInterfaceStyle.UIUserInterfaceStyleDark else UIUserInterfaceStyle.UIUserInterfaceStyleLight
        tabBar.tintColor = accent.toUIColor()
    }
}

@OptIn(ExperimentalForeignApi::class)
private class MeasuredTabBar(
    private val onHeightChanged: (Double) -> Unit,
) : UITabBar(frame = CGRectZero.readValue()) {

    private var lastHeight = -1.0
    private var shownItems: List<AppNavigationBarItem>? = null

    /** Only recreates the native items when they changed, e.g. when the language changed. */
    fun updateItems(items: List<AppNavigationBarItem>) {
        if (items == shownItems) return
        shownItems = items
        setItems(
            items.mapIndexed { index, item ->
                UITabBarItem(
                    title = item.label,
                    image = UIImage.systemImageNamed(item.iosSystemImageName),
                    tag = index.toLong(),
                )
            },
            animated = false,
        )
    }

    override fun layoutSubviews() {
        super.layoutSubviews()
        val height = bounds.useContents { size.height }
        if (height != lastHeight) {
            lastHeight = height
            onHeightChanged(height)
        }
    }
}

private class TabBarDelegate(
    private val onItemSelected: (Int) -> Unit,
) : NSObject(), UITabBarDelegateProtocol {
    override fun tabBar(tabBar: UITabBar, didSelectItem: UITabBarItem) {
        onItemSelected(didSelectItem.tag.toInt())
    }
}
