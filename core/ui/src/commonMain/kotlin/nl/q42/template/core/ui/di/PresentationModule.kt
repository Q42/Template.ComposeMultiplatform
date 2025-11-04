package nl.q42.template.core.ui.di

import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.core.ui.presentation.dialog.DialogPresenter
import nl.q42.template.core.ui.presentation.dialog.DialogPresenterImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val presentationModule = module {

    factoryOf(::DialogPresenterImpl) { bind<DialogPresenter>() }

    singleOf(::SnackbarManager)
}
