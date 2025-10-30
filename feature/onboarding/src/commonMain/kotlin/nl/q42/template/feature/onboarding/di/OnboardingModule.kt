package nl.q42.template.feature.onboarding.di

import nl.q42.template.feature.onboarding.presentation.OnboardingViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
val onboardingModule = module {

    viewModelOf(::OnboardingViewModel)
}