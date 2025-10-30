package nl.q42.template.core.ui.di

import nl.q42.template.core.ui.presentation.dialog.DialogPresenter
import nl.q42.template.core.ui.presentation.dialog.DialogPresenterImpl
import org.koin.dsl.module

val presentationModule = module {

//    singleOf(::SnackbarManager)

    factory<DialogPresenter> { DialogPresenterImpl() }
}
