package ir.rabinn.greatwall.model

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val appName: String,
    val isSystemApp: Boolean,
    val icon: Drawable
) {
    companion object {
        fun fromPackageInfo(pm: PackageManager, packageName: String): AppInfo? {
            return try {
                val appInfo = pm.getApplicationInfo(packageName, 0)
                val appName = pm.getApplicationLabel(appInfo).toString()
                val icon = pm.getApplicationIcon(appInfo)
                val isSystem = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                AppInfo(packageName, appName, isSystem, icon)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }
    }
}
