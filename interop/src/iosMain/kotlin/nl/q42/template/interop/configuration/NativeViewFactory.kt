package nl.q42.template.interop.configuration

import platform.UIKit.UIViewController
import kotlin.experimental.ExperimentalObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName(swiftName = "NativeViewFactory")
interface NativeViewFactory {
    fun createButton(text: String, onClick: () -> Unit): UIViewController
}