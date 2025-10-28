package nl.q42.template.core.ui.di

import nl.q42.template.core.ui.presentation.dialog.DialogPresenter
import nl.q42.template.core.ui.presentation.dialog.DialogPresenterImpl
import org.koin.dsl.module

internal val presentationModule = module {

// TODO:
//    fun provideAccessibilityManager(application: Application): AccessibilityManager =
//        application.getSystemService(
//            Application.ACCESSIBILITY_SERVICE
//        ) as AccessibilityManager


    factory<DialogPresenter> { DialogPresenterImpl() }
}
