package nl.q42.template.data.main.di

import nl.q42.template.data.main.AppSettingsRepositoryImpl
import nl.q42.template.data.main.UserRepositoryImpl
import nl.q42.template.data.main.local.UserLocalDataSource
import nl.q42.template.data.main.remote.UserRemoteDataSource
import nl.q42.template.data.main.remote.api.UserApi
import nl.q42.template.domain.main.repo.AppSettingsRepository
import nl.q42.template.domain.main.repo.UserRepository
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.plugin.module.dsl.bind
import org.koin.plugin.module.dsl.single

expect val dataPlatformModule: Module

val mainDataModule = module {

    includes(dataPlatformModule)

    single<UserRemoteDataSource>()

    single<UserLocalDataSource>()

    single<UserRepositoryImpl>().bind(UserRepository::class)

    single<AppSettingsRepositoryImpl>().bind(AppSettingsRepository::class)

    single<UserApi>()
}

internal const val DATA_STORE_FILE_NAME = "app_settings.preferences_pb"
