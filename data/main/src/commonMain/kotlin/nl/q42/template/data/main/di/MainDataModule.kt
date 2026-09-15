package nl.q42.template.data.main.di

import nl.q42.template.data.main.SampleAppSettingsRepositoryImpl
import nl.q42.template.data.main.SampleUserRepositoryImpl
import nl.q42.template.data.main.local.SampleUserLocalDataSource
import nl.q42.template.data.main.remote.SampleUserRemoteDataSource
import nl.q42.template.data.main.remote.api.SampleUserApi
import nl.q42.template.domain.main.repo.SampleAppSettingsRepository
import nl.q42.template.domain.main.repo.SampleUserRepository
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.plugin.module.dsl.bind
import org.koin.plugin.module.dsl.single

expect val dataPlatformModule: Module

val mainDataModule = module {

    includes(dataPlatformModule)

    single<SampleUserRemoteDataSource>()

    single<SampleUserLocalDataSource>()

    single<SampleUserRepositoryImpl>().bind(SampleUserRepository::class)

    single<SampleAppSettingsRepositoryImpl>().bind(SampleAppSettingsRepository::class)

    single<SampleUserApi>()
}

internal const val DATA_STORE_FILE_NAME = "app_settings.preferences_pb"
