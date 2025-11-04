package nl.q42.template.di

import nl.q42.template.interop.NativeDependencyExample
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinDi {
    fun initializeKoin(interopProvider: NativeDependencyExample) {
        startKoin {
            modules(
                module {
                    single<NativeDependencyExample> { interopProvider }
                },
                appModules
            )
        }
    }
}
