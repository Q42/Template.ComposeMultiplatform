package nl.q42.template.domain.main.usecase

actual suspend fun getPlatformGreeting(userName: String): String {
    // Context could be accessed here by:
    // val context: Context by inject(Context::class.java)

    return "Hello from Android, $userName!"
}