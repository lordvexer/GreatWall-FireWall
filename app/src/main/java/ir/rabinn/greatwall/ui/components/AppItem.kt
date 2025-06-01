package ir.rabinn.greatwall.ui.components

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap

@Composable
fun AppItem(appInfo: ApplicationInfo, pm: PackageManager) {
    var wifiEnabled by remember { mutableStateOf(true) }
    var dataEnabled by remember { mutableStateOf(true) }

    val iconBitmap = remember(appInfo.packageName) {
        try {
            pm.getApplicationIcon(appInfo).toBitmap().asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconBitmap?.let {
            Image(bitmap = it, contentDescription = null, modifier = Modifier.size(36.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = pm.getApplicationLabel(appInfo).toString(),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = appInfo.packageName,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Wi-Fi", style = MaterialTheme.typography.labelSmall)
            Switch(
                checked = wifiEnabled,
                onCheckedChange = { wifiEnabled = it },
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Data", style = MaterialTheme.typography.labelSmall)
            Switch(
                checked = dataEnabled,
                onCheckedChange = { dataEnabled = it },
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
