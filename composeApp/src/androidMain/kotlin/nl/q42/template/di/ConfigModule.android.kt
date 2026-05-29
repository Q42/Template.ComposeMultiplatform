package nl.q42.template.di

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.core.content.pm.PackageInfoCompat
import org.koin.java.KoinJavaComponent.inject

actual fun isDebug(): Boolean {
    val context: Context by inject(Context::class.java)
    return context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
}

actual fun getAppVersionName(): String? {
    val context: Context by inject(Context::class.java)
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName
}

actual fun getAppVersionCode(): Long {
    val context: Context by inject(Context::class.java)
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return PackageInfoCompat.getLongVersionCode(packageInfo)
}

actual fun getApplicationId(): String? {
    val context: Context by inject(Context::class.java)
    return context.packageName
}