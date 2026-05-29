package nl.q42.template.di

import android.content.Context
import android.content.pm.ApplicationInfo
import org.koin.java.KoinJavaComponent.inject

actual fun isDebug(): Boolean {
    val context: Context by inject(Context::class.java)
    return context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
}

actual fun getApplicationId(): String? {
    val context: Context by inject(Context::class.java)
    return context.packageName
}