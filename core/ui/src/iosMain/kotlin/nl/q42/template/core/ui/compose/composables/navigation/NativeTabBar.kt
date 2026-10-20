package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import androidx.compose.ui.viewinterop.rememberUIKitInteropRemeasureRequester
import androidx.compose.ui.viewinterop.remeasureRequester
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.LocalThemeIsDark
import nl.q42.template.core.ui.util.toUIColor
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIImage
import platform.UIKit.UITabBar
import platform.UIKit.UITabBarDelegateProtocol
import platform.UIKit.UITabBarItem
import platform.UIKit.UIUserInterfaceStyle
import platform.darwin.NSObject

/**
 * Native UITabBar. UIKit takes care of the Liquid Glass look, VoiceOver, safe areas and rotation.
 * [addLargeContentViewer] adds the Large Content Viewer for large Dynamic Type sizes.
 *
 * Placed as an overlay, so it is drawn on top of the Compose content and the Liquid Glass can show it.
 * Compose measures its height with Auto Layout.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun NativeTabBar(
    items: List<AppNavigationBarItem>,
    selectedIndex: Int,
    onItemSelected: (index: Int) -> Unit,
    onHeightChanged: (Dp) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnItemSelected by rememberUpdatedState(onItemSelected)
    // UIKit holds delegates weakly, so we hold on to them here
    val delegate = remember { TabBarDelegate { currentOnItemSelected(it) } }
    val largeContentViewerDelegate = remember { TabBarLargeContentViewerDelegate { currentOnItemSelected(it) } }
    val remeasureRequester = rememberUIKitInteropRemeasureRequester()
    val density = LocalDensity.current
    val isDark = LocalThemeIsDark.current.value
    val accent = AppTheme.colors.accent

    UIKitView(
        factory = {
            SafeAreaAwareTabBar(onSafeAreaChanged = { remeasureRequester.requestRemeasure() }).apply {
                this.delegate = delegate
                addLargeContentViewer(largeContentViewerDelegate)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .remeasureRequester(remeasureRequester)
            .onSizeChanged { size -> onHeightChanged(with(density) { size.height.toDp() }) },
        update = { tabBar ->
            tabBar.updateItems(items)
            tabBar.selectedItem = tabBar.items?.getOrNull(selectedIndex) as? UITabBarItem
            // Follow the app theme rather than the system, in case the app overrides dark mode
            tabBar.overrideUserInterfaceStyle =
                if (isDark) UIUserInterfaceStyle.UIUserInterfaceStyleDark else UIUserInterfaceStyle.UIUserInterfaceStyleLight
            tabBar.tintColor = accent.toUIColor()
        },
        properties = UIKitInteropProperties(
            // Tabs respond immediately, there is no Compose scrolling to wait for
            interactionMode = UIKitInteropInteractionMode.NonCooperative,
            isNativeAccessibilityEnabled = true,
            placedAsOverlay = true,
        ),
    )
}

/**
 * UIKit adds the part of the bottom safe area the bar overlaps to its height. That part is only known once the bar
 * is placed, so Compose is asked to measure again when it changes.
 */
@OptIn(ExperimentalForeignApi::class)
private class SafeAreaAwareTabBar(
    private val onSafeAreaChanged: () -> Unit,
) : UITabBar(frame = CGRectZero.readValue()) {

    private var shownItems: List<AppNavigationBarItem>? = null

    /** Only recreates the native items when they changed */
    fun updateItems(items: List<AppNavigationBarItem>) {
        if (items == shownItems) return
        shownItems = items
        setItems(
            items.mapIndexed { index, item ->
                UITabBarItem(
                    title = item.label,
                    image = UIImage.imageNamed(item.iosImageName),
                    tag = index.toLong(),
                )
            },
            animated = false,
        )
    }

    override fun safeAreaInsetsDidChange() {
        super.safeAreaInsetsDidChange()
        onSafeAreaChanged()
    }
}

private class TabBarDelegate(
    private val onItemSelected: (Int) -> Unit,
) : NSObject(), UITabBarDelegateProtocol {
    override fun tabBar(tabBar: UITabBar, didSelectItem: UITabBarItem) {
        onItemSelected(didSelectItem.tag.toInt())
    }
}
