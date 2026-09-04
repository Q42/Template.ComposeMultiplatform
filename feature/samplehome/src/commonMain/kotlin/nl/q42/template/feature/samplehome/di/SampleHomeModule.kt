package nl.q42.template.feature.samplehome.di

import nl.q42.template.feature.samplehome.presentation.SampleHomeViewModel
import nl.q42.template.feature.samplehome.presentation.SampleInteropViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val sampleHomeModule = module {
    viewModelOf(::SampleHomeViewModel)
    viewModelOf(::SampleInteropViewModel)
}