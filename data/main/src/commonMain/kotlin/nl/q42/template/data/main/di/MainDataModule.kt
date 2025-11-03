package nl.q42.template.data.main.di

import nl.q42.template.data.main.UserRepositoryImpl
import nl.q42.template.data.main.local.UserLocalDataSource
import nl.q42.template.data.main.remote.UserRemoteDataSource
import nl.q42.template.domain.main.repo.UserRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val mainDataModule = module {

    singleOf(::UserRemoteDataSource)

    singleOf(::UserLocalDataSource)

    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
}