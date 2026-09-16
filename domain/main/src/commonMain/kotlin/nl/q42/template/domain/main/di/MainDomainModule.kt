package nl.q42.template.domain.main.di

import nl.q42.template.domain.main.usecase.ExecuteNativeAsyncExampleMethodUseCase
import nl.q42.template.domain.main.usecase.ExecuteNativeExampleMethodUseCase
import nl.q42.template.domain.main.usecase.FetchUserUseCase
import nl.q42.template.domain.main.usecase.GetUserFlowUseCase
import nl.q42.template.domain.main.usecase.GetPlatformUserGreetingFlowUseCase
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val domainModule = module {
    factory<FetchUserUseCase>()
    factory<GetUserFlowUseCase>()
    factory<ExecuteNativeExampleMethodUseCase>()
    factory<ExecuteNativeAsyncExampleMethodUseCase>()
    factory<GetPlatformUserGreetingFlowUseCase>()
}

