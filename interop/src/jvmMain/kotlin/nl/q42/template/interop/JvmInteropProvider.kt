package nl.q42.template.interop


class JvmInteropProvider: InteropProvider {
    override fun provideExampleText(): String {
        return "Hello from JVM!"
    }
}