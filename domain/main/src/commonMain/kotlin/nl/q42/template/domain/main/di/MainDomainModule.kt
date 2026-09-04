package nl.q42.template.domain.main.di

import nl.q42.template.domain.main.usecase.SampleExecuteNativeAsyncMethodUseCase
import nl.q42.template.domain.main.usecase.SampleExecuteNativeMethodUseCase
import nl.q42.template.domain.main.usecase.SampleFetchUserUseCase
import nl.q42.template.domain.main.usecase.SampleGetUserFlowUseCase
import nl.q42.template.domain.main.usecase.SampleGetPlatformUserGreetingFlowUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::SampleFetchUserUseCase)
    factoryOf(::SampleGetUserFlowUseCase)
    factoryOf(::SampleExecuteNativeMethodUseCase)
    factoryOf(::SampleExecuteNativeAsyncMethodUseCase)
    factoryOf(::SampleGetPlatformUserGreetingFlowUseCase)
}

