package nl.q42.template.feature.onboarding.di

import nl.q42.template.feature.onboarding.presentation.OnboardingViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

@OptIn(KoinExperimentalAPI::class)
val onboardingModule = module {

    viewModel<OnboardingViewModel>()
}
