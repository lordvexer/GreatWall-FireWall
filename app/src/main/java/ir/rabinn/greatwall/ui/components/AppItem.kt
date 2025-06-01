package ir.rabinn.greatwall.ui.components

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.style.TextOverflow

// اکستنشن تبدیل Drawable به Bitmap
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        bitmap?.let { return it }
    }

    val bitmap = Bitmap.createBitmap(
        intrinsicWidth.takeIf { it > 0 } ?: 1,
        intrinsicHeight.takeIf { it > 0 } ?: 1,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}

@Composable
fun AppItem(
    appInfo: ApplicationInfo,
    pm: PackageManager,
    wifiEnabled: Boolean,
    onWifiToggle: (Boolean) -> Unit,
    mobileEnabled: Boolean,
    onMobileToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // آیکون برنامه
        val icon = appInfo.loadIcon(pm)
        Image(
            bitmap = icon.toBitmap().asImageBitmap(),
            contentDescription = appInfo.loadLabel(pm).toString(),
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // نام برنامه با محدودیت طول و کوتاه شدن متن
        Text(
            text = appInfo.loadLabel(pm).toString(),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // تاگل وای‌فای با اندازه کوچک
        Switch(
            checked = wifiEnabled,
            onCheckedChange = onWifiToggle,
            modifier = Modifier
                .size(32.dp, 20.dp)  // اندازه مناسب برای کوچک شدن
                // اگر بخواهی scale بدی، بهتر اینجا از scale استفاده نکنی چون اندازه مشخص داری
        )

        Spacer(modifier = Modifier.width(8.dp))

        // تاگل دیتا با اندازه کوچک
        Switch(
            checked = mobileEnabled,
            onCheckedChange = onMobileToggle,
            modifier = Modifier
                .size(32.dp, 20.dp)  // اندازه مساوی وای‌فای، تا مرتب باشن
        )
    }
}
