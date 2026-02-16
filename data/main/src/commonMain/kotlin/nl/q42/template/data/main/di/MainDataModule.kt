package nl.q42.template.data.main.di

import nl.q42.template.data.main.AppSettingsRepositoryImpl
import nl.q42.template.data.main.UserRepositoryImpl
import nl.q42.template.data.main.local.UserLocalDataSource
import nl.q42.template.data.main.remote.UserRemoteDataSource
import nl.q42.template.data.main.remote.api.UserApi
import nl.q42.template.domain.main.repo.AppSettingsRepository
import nl.q42.template.domain.main.repo.UserRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val dataStoreModule: Module

val mainDataModule = module {

    includes(dataStoreModule)

    singleOf(::UserRemoteDataSource)

    singleOf(::UserLocalDataSource)

    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }

    singleOf(::AppSettingsRepositoryImpl) { bind<AppSettingsRepository>() }

    singleOf(::UserApi)
}

internal const val dataStoreFileName = "app_settings.preferences_pb"
