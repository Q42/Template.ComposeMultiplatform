package nl.q42.template.core.ui.compose.composables.navigation

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGPoint
import platform.CoreGraphics.CGRectGetMidX
import platform.UIKit.UIEdgeInsetsZero
import platform.UIKit.UIGestureRecognizer
import platform.UIKit.UILargeContentViewerInteraction
import platform.UIKit.UILargeContentViewerInteractionDelegateProtocol
import platform.UIKit.UILargeContentViewerItemProtocol
import platform.UIKit.UITabBar
import platform.UIKit.UITabBarItem
import platform.UIKit.UIView
import platform.UIKit.addInteraction
import platform.UIKit.largeContentTitle
import platform.darwin.NSObject
import kotlin.math.abs

/**
 * Tab labels don't grow with Dynamic Type, like in every iOS tab bar. Instead, at the accessibility text sizes a long
 * press shows the tab enlarged in the middle of the screen: the Large Content Viewer.
 * A UITabBarController sets this up, a standalone UITabBar doesn't.
 *
 * UIKit holds [delegate] weakly, so the caller has to hold on to it.
 */
internal fun UITabBar.addLargeContentViewer(delegate: TabBarLargeContentViewerDelegate) {
    val tabBarGestures = gestureRecognizers.orEmpty().filterIsInstance<UIGestureRecognizer>()
    val interaction = UILargeContentViewerInteraction(delegate)
    addInteraction(interaction)
    // Otherwise the Liquid Glass selection gesture starts right away and the long press never does.
    // Only the accessibility text sizes enable the long press; at other sizes it fails right away.
    tabBarGestures.forEach { it.requireGestureRecognizerToFail(interaction.gestureRecognizerForExclusionRelationship) }
}

@OptIn(ExperimentalForeignApi::class)
internal class TabBarLargeContentViewerDelegate(
    private val onItemSelected: (index: Int) -> Unit,
) : NSObject(), UILargeContentViewerInteractionDelegateProtocol {

    override fun largeContentViewerInteraction(
        interaction: UILargeContentViewerInteraction,
        itemAtPoint: CValue<CGPoint>,
    ): UILargeContentViewerItemProtocol? {
        val tabBar = interaction.view as? UITabBar ?: return null
        return tabBar.itemAt(itemAtPoint)?.let(::TabBarLargeContentItem)
    }

    // Lifting the finger selects the tab, like a tap
    override fun largeContentViewerInteraction(
        interaction: UILargeContentViewerInteraction,
        didEndOnItem: UILargeContentViewerItemProtocol?,
        atPoint: CValue<CGPoint>,
    ) {
        (didEndOnItem as? TabBarLargeContentItem)?.let { onItemSelected(it.tabBarItem.tag.toInt()) }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class TabBarLargeContentItem(
    val tabBarItem: UITabBarItem,
) : NSObject(), UILargeContentViewerItemProtocol {
    override fun showsLargeContentViewer() = true
    override fun largeContentTitle() = tabBarItem.title
    override fun largeContentImage() = tabBarItem.image
    override fun scalesLargeContentImage() = true
    override fun largeContentImageInsets() = UIEdgeInsetsZero.readValue()
}

/** UIKit has no API for the frame of a tab, so we look for the tab title shown closest to the point. */
@OptIn(ExperimentalForeignApi::class)
private fun UITabBar.itemAt(point: CValue<CGPoint>): UITabBarItem? {
    // Null when the point is next to the floating bar
    val hitView = hitTest(point, withEvent = null) ?: return null
    val x = point.useContents { x }
    val tabBarItems = items.orEmpty().filterIsInstance<UITabBarItem>()
    return hitView.withDescendants()
        .mapNotNull { view ->
            val title = view.largeContentTitle ?: return@mapNotNull null
            tabBarItems.firstOrNull { it.title == title }?.let { item -> item to view }
        }
        .minByOrNull { (_, view) -> abs(CGRectGetMidX(view.convertRect(view.bounds, toView = this)) - x) }
        ?.first
}

private fun UIView.withDescendants(): Sequence<UIView> =
    sequenceOf(this) + subviews.asSequence().filterIsInstance<UIView>().flatMap { it.withDescendants() }
