package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import nl.q42.template.core.ui.compose.LocalNativeViewFactory
import platform.UIKit.UILayoutFittingCompressedSize

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeButton(
    text: String,
    onClick: () -> Unit,
    modifier: androidx.compose.ui.Modifier
) {
    val nativeViewFactory = LocalNativeViewFactory.current
    UIKitViewController(
        factory = {
            nativeViewFactory.createButton(text, onClick)
        },
        modifier = modifier
    )
}