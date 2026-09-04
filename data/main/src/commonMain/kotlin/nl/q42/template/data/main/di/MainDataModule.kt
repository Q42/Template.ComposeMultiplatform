package nl.q42.template.data.main.di

import nl.q42.template.data.main.SampleAppSettingsRepositoryImpl
import nl.q42.template.data.main.SampleUserRepositoryImpl
import nl.q42.template.data.main.local.SampleUserLocalDataSource
import nl.q42.template.data.main.remote.SampleUserRemoteDataSource
import nl.q42.template.data.main.remote.api.SampleUserApi
import nl.q42.template.domain.main.repo.SampleAppSettingsRepository
import nl.q42.template.domain.main.repo.SampleUserRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val dataPlatformModule: Module

val mainDataModule = module {

    includes(dataPlatformModule)

    singleOf(::SampleUserRemoteDataSource)

    singleOf(::SampleUserLocalDataSource)

    singleOf(::SampleUserRepositoryImpl) { bind<SampleUserRepository>() }

    singleOf(::SampleAppSettingsRepositoryImpl) { bind<SampleAppSettingsRepository>() }

    singleOf(::SampleUserApi)
}

internal const val DATA_STORE_FILE_NAME = "app_settings.preferences_pb"
