package nl.q42.template.core.ui.di

import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.core.ui.presentation.dialog.DialogPresenter
import nl.q42.template.core.ui.presentation.dialog.DialogPresenterImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val presentationModule = module {

// TODO:
//    fun provideAccessibilityManager(application: Application): AccessibilityManager =
//        application.getSystemService(
//            Application.ACCESSIBILITY_SERVICE
//        ) as AccessibilityManager

    singleOf(::SnackbarManager)

    factory<DialogPresenter> { DialogPresenterImpl() }
}
