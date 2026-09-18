package nl.q42.template.domain.main.di

import nl.q42.template.domain.main.usecase.SampleExecuteNativeAsyncMethodUseCase
import nl.q42.template.domain.main.usecase.SampleExecuteNativeMethodUseCase
import nl.q42.template.domain.main.usecase.SampleFetchUserUseCase
import nl.q42.template.domain.main.usecase.SampleGetPlatformUserGreetingFlowUseCase
import nl.q42.template.domain.main.usecase.SampleGetUserFlowUseCase
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val domainModule = module {
    factory<SampleFetchUserUseCase>()
    factory<SampleGetUserFlowUseCase>()
    factory<SampleExecuteNativeMethodUseCase>()
    factory<SampleExecuteNativeAsyncMethodUseCase>()
    factory<SampleGetPlatformUserGreetingFlowUseCase>()
}

