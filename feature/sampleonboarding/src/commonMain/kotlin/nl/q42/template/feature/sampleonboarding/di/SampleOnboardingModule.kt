package nl.q42.template.feature.sampleonboarding.di

import nl.q42.template.feature.sampleonboarding.presentation.SampleOnboardingViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

@OptIn(KoinExperimentalAPI::class)
val sampleOnboardingModule = module {
    viewModel<SampleOnboardingViewModel>()
}
