package nl.q42.template.di

actual fun isDebug(): Boolean {
    return System.getProperty("debug") != null || System.getenv("DEBUG") != null
}

actual fun getApplicationId(): String? {
    return null
}