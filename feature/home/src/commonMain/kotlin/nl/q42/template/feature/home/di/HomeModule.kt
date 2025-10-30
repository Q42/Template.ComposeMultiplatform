package nl.q42.template.feature.home.di

import nl.q42.template.core.ui.di.presentationModule
import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.data.main.di.mainDataModule
import nl.q42.template.domain.main.usecase.FetchUserUseCase
import nl.q42.template.domain.main.usecase.GetUserFlowUseCase
import nl.q42.template.feature.home.presentation.HomeViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val homeModule = module {

    includes(mainDataModule)
    includes(presentationModule)

    factoryOf(::FetchUserUseCase)
    factoryOf(::GetUserFlowUseCase)
    factoryOf(::SnackbarManager)

    viewModel<HomeViewModel> {
        HomeViewModel(
            fetchUserUseCase = get(),
            getUserFlowUseCase = get(),
            // TODO: navigator = get(),
            snackbarManager = get(),
            dialogPresenter = get()
        )
    }

    scope<HomeViewModel> {

        scopedOf(::FetchUserUseCase)
        scopedOf(::GetUserFlowUseCase)
        scopedOf(::SnackbarManager)

    }
}