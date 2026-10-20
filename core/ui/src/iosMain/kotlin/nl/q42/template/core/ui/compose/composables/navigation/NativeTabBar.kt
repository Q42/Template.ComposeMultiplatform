package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
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
import platform.UIKit.UIView
import platform.darwin.NSObject

/**
 * Native UITabBar, added on top of the Compose view. UIKit takes care of the Liquid Glass look,
 * accessibility (VoiceOver, Large Content Viewer for large Dynamic Type sizes), safe areas and rotation.
 *
 * An empty Compose placeholder with [modifier] and the height of the tab bar is laid out in its place,
 * so callers can position it like any composable. The tab bar follows the bounds of that placeholder.
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
    modifier: Modifier = Modifier,
) {
    val rootView = LocalUIViewController.current.view
    val density = LocalDensity.current.density
    val currentOnItemSelected by rememberUpdatedState(onItemSelected)
    val currentOnHeightChanged by rememberUpdatedState(onHeightChanged)

    // UITabBar.delegate is weak, so we hold on to it here
    val delegate = remember { TabBarDelegate { currentOnItemSelected(it) } }
    var height by remember { mutableStateOf(0.dp) }
    val tabBar = remember {
        MeasuredTabBar {
            height = it.dp
            currentOnHeightChanged(it.dp)
        }.apply {
            translatesAutoresizingMaskIntoConstraints = false
            this.delegate = delegate
        }
    }
    val position = remember(rootView) { TabBarPosition(tabBar, rootView) }

    DisposableEffect(position) {
        rootView.addSubview(tabBar)
        NSLayoutConstraint.activateConstraints(position.constraints)
        onDispose {
            tabBar.removeFromSuperview()
            currentOnHeightChanged(0.dp)
        }
    }

    Spacer(
        modifier
            .fillMaxWidth()
            .height(height)
            .onGloballyPositioned { position.update(it.boundsInRoot(), it.findRootCoordinates().size, density) }
    )

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

/**
 * Pins the tab bar to the bounds of its Compose placeholder. The tab bar sets its own height: when it touches
 * the bottom of the screen, UIKit adds the bottom safe area to it.
 *
 * The constraints are offsets from the edges of the root view. They start at 0 (full width, at the bottom), because
 * the item labels stay truncated after a first layout at a width of 0.
 */
private class TabBarPosition(tabBar: UITabBar, rootView: UIView) {
    private val leading = tabBar.leadingAnchor.constraintEqualToAnchor(rootView.leadingAnchor)
    private val trailing = tabBar.trailingAnchor.constraintEqualToAnchor(rootView.trailingAnchor)
    private val bottom = tabBar.bottomAnchor.constraintEqualToAnchor(rootView.bottomAnchor)

    val constraints = listOf(leading, trailing, bottom)

    /** @param boundsInRoot and [rootSize] in pixels. The Compose root fills the root view. */
    fun update(boundsInRoot: Rect, rootSize: IntSize, density: Float) {
        leading.constant = (boundsInRoot.left / density).toDouble()
        trailing.constant = ((boundsInRoot.right - rootSize.width) / density).toDouble()
        bottom.constant = ((boundsInRoot.bottom - rootSize.height) / density).toDouble()
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
