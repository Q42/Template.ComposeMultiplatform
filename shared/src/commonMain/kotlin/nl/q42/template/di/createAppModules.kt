package nl.q42.template.di

import nl.q42.template.core.navigation.viewmodel.NavigationBackStackHolder
import nl.q42.template.core.network.di.networkModule
import nl.q42.template.core.ui.di.presentationModule
import nl.q42.template.data.main.di.mainDataModule
import nl.q42.template.domain.main.di.domainModule
import nl.q42.template.domain.main.usecase.ExecuteNativeAsyncExampleMethodUseCase
import nl.q42.template.domain.main.usecase.ExecuteNativeExampleMethodUseCase
import nl.q42.template.domain.main.usecase.FetchUserUseCase
import nl.q42.template.domain.main.usecase.GetUserFlowUseCase
import nl.q42.template.feature.home.di.homeModule
import nl.q42.template.feature.onboarding.di.onboardingModule
import nl.q42.template.interop.NativeDependencyExample
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
fun createAppModules(nativeDependencyExample: NativeDependencyExample) = module {

    includes(configModule)
    includes(networkModule)
    includes(mainDataModule)
    includes(domainModule)
    includes(presentationModule)
    includes(homeModule)
    includes(onboardingModule)

    single { NavigationBackStackHolder() }
    single { nativeDependencyExample }
}