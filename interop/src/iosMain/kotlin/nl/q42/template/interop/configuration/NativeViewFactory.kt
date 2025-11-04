package nl.q42.template.interop.configuration

import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createButton(text: String, onClick: () -> Unit): UIViewController
}