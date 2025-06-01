package ir.rabinn.greatwall.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

object AppUtils {

    /**
     * دریافت لیست اپلیکیشن‌ها با امکان فیلتر برنامه‌های سیستمی یا غیرسیستمی
     */
    fun getInstalledApps(context: Context, showSystemApps: Boolean): List<ApplicationInfo> {
        val pm = context.packageManager
        return pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { app ->
                if (showSystemApps) true
                else (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0
            }
            .sortedBy { it.loadLabel(pm).toString().lowercase() }
    }

}
