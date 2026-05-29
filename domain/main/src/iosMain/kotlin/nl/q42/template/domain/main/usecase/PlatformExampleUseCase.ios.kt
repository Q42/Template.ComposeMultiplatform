package nl.q42.template.domain.main.usecase

actual suspend fun getPlatformGreeting(userName: String): String {
    return "Hello from iOS, $userName!"
}