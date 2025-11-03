package nl.q42.template.core.utils.interop

class JvmInteropProvider: InteropProvider {
    override fun provideExampleText(): String {
        return "Hello from JVM!"
    }
}