package nl.q42.template.feature.samplehome.di

import nl.q42.template.feature.samplehome.presentation.SampleHomeViewModel
import nl.q42.template.feature.samplehome.presentation.SampleInteropViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

@OptIn(KoinExperimentalAPI::class)
val sampleHomeModule = module {
    viewModel<SampleHomeViewModel>()
    viewModel<SampleInteropViewModel>()
}
