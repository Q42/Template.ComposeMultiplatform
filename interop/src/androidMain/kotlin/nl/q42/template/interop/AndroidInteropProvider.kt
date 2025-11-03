package nl.q42.template.interop

class AndroidInteropProvider: InteropProvider {
    override fun provideExampleText(): String {
        return "Hello from Android!"
    }
}