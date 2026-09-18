package nl.q42.template.di

import nl.q42.template.core.navigation.viewmodel.NavigationBackStackHolder
import nl.q42.template.core.network.di.networkModule
import nl.q42.template.core.ui.di.presentationModule
import nl.q42.template.data.main.di.mainDataModule
import nl.q42.template.domain.main.di.domainModule
import nl.q42.template.feature.samplehome.di.sampleHomeModule
import nl.q42.template.feature.sampleonboarding.di.sampleOnboardingModule
import nl.q42.template.interop.SampleNativeDependency
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
internal fun createAppModules(sampleNativeDependency: SampleNativeDependency) = module {

    includes(configModule)
    includes(networkModule)
    includes(mainDataModule)
    includes(domainModule)
    includes(presentationModule)
    includes(sampleHomeModule)
    includes(sampleOnboardingModule)

    single { NavigationBackStackHolder() }
    single { sampleNativeDependency }
}
