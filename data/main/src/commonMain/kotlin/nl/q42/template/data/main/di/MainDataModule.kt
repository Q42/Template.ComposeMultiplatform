package nl.q42.template.data.main.di

import nl.q42.template.data.main.UserRepositoryImpl
import nl.q42.template.data.main.local.UserLocalDataSource
import nl.q42.template.data.main.remote.UserRemoteDataSource
import nl.q42.template.domain.main.repo.UserRepository
import org.koin.dsl.module

internal val mainDataModule = module {

    single { UserRemoteDataSource() }

    single { UserLocalDataSource() }

    single<UserRepository> {
        UserRepositoryImpl(
            userRemoteDataSource = get(),
            userLocalDataSource = get()
        )
    }
}