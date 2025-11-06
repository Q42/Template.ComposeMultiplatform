package nl.q42.template.di

import nl.q42.template.core.navigation.viewmodel.MyRouteNavigator
import nl.q42.template.core.navigation.viewmodel.RouteNavigator
import nl.q42.template.core.ui.di.presentationModule
import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.data.main.di.mainDataModule
import nl.q42.template.domain.main.usecase.ExecuteNativeAsyncExampleMethodUseCase
import nl.q42.template.domain.main.usecase.ExecuteNativeExampleMethodUseCase
import nl.q42.template.domain.main.usecase.FetchUserUseCase
import nl.q42.template.domain.main.usecase.GetUserFlowUseCase
import nl.q42.template.feature.home.di.homeModule
import nl.q42.template.feature.onboarding.di.onboardingModule
import nl.q42.template.interop.NativeDependencyExample
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
fun createAppModules(nativeDependencyExample: NativeDependencyExample) = module {

    includes(mainDataModule)
    includes(presentationModule)
    includes(homeModule)
    includes(onboardingModule)

    factoryOf(::FetchUserUseCase)
    factoryOf(::GetUserFlowUseCase)
    factoryOf(::ExecuteNativeExampleMethodUseCase)
    factoryOf(::ExecuteNativeAsyncExampleMethodUseCase)

    singleOf(::MyRouteNavigator) { bind<RouteNavigator>() }
    single { nativeDependencyExample }
}