package nl.q42.template.di

actual fun isDebug(): Boolean {
    return System.getProperty("debug") != null || System.getenv("DEBUG") != null
}

actual fun getAppVersionName(): String? {
    return "1.0.0"
}

actual fun getAppVersionCode(): Long {
    return 1L
}

actual fun getApplicationId(): String? {
    return "nl.q42.template"
}