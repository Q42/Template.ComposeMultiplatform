package nl.q42.template.feature.home.di

import nl.q42.template.feature.home.presentation.HomeViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val homeModule = module {

    viewModelOf(::HomeViewModel)
}