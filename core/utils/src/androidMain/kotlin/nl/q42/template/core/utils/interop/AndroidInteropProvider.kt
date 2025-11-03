package nl.q42.template.core.utils.interop

class AndroidInteropProvider: InteropProvider {
    override fun provideExampleText(): String {
        return "Hello from Android!"
    }
}