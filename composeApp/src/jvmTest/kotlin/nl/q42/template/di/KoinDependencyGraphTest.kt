package nl.q42.template.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.verify.verify
import kotlin.test.AfterTest
import kotlin.test.Test

/**
 * Test to verify the Koin dependency graph is correctly configured.
 * This test ensures all dependencies can be resolved without runtime errors.
 *
 * Note: This test is JVM-only as it uses the verify() API which is not available on all platforms.
 */
@OptIn(KoinExperimentalAPI::class)
class KoinDependencyGraphTest : KoinTest {

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    /**
     * Verifies that all modules are correctly configured using Koin's verify() API.
     * This checks the module definitions at compile time where possible.
     */
    @Test
    fun `verify module definitions are valid`() {
        appModules.verify()
    }
}

