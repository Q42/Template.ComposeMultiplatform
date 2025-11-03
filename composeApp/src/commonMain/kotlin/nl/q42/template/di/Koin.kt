package nl.q42.template.di

import nl.q42.template.interop.InteropProvider
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinDi {
    fun initializeKoin(interopProvider: InteropProvider) {
        startKoin {
            modules(
                module {
                    single<InteropProvider> { interopProvider }
                },
                appModules
            )
        }
    }
}
