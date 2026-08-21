package nl.q42.template.core.ui.di

import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.core.ui.presentation.dialog.DialogPresenter
import nl.q42.template.core.ui.presentation.dialog.DialogPresenterImpl
import org.koin.dsl.module
import org.koin.plugin.module.dsl.bind
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.single

val presentationModule = module {

    factory<DialogPresenterImpl>().bind(DialogPresenter::class)

    single<SnackbarManager>()
}
