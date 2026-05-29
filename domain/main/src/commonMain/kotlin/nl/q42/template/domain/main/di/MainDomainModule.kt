package nl.q42.template.domain.main.di

import nl.q42.template.domain.main.usecase.ExecuteNativeAsyncExampleMethodUseCase
import nl.q42.template.domain.main.usecase.ExecuteNativeExampleMethodUseCase
import nl.q42.template.domain.main.usecase.FetchUserUseCase
import nl.q42.template.domain.main.usecase.GetUserFlowUseCase
import nl.q42.template.domain.main.usecase.GetPlatformUserGreetingFlowUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::FetchUserUseCase)
    factoryOf(::GetUserFlowUseCase)
    factoryOf(::ExecuteNativeExampleMethodUseCase)
    factoryOf(::ExecuteNativeAsyncExampleMethodUseCase)
    factoryOf(::GetPlatformUserGreetingFlowUseCase)
}

