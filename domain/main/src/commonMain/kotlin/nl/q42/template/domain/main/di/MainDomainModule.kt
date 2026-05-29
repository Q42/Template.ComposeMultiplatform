package nl.q42.template.domain.main.di

import nl.q42.template.domain.main.usecase.ExecuteNativeAsyncExampleMethodUseCase
import nl.q42.template.domain.main.usecase.ExecuteNativeExampleMethodUseCase
import nl.q42.template.domain.main.usecase.FetchUserUseCase
import nl.q42.template.domain.main.usecase.GetUserFlowUseCase
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

expect val domainPlatformModule: Module

val domainModule = module {

    includes(domainPlatformModule)

    factoryOf(::FetchUserUseCase)
    factoryOf(::GetUserFlowUseCase)
    factoryOf(::ExecuteNativeExampleMethodUseCase)
    factoryOf(::ExecuteNativeAsyncExampleMethodUseCase)
}

