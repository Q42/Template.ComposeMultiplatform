package nl.q42.template.feature.home.di

import nl.q42.template.feature.home.presentation.HomeViewModel
import nl.q42.template.feature.home.presentation.InteropExamplesViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

@OptIn(KoinExperimentalAPI::class)
val homeModule = module {
    viewModel<HomeViewModel>()
    viewModel<InteropExamplesViewModel>()
}
