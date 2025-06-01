package ir.rabinn.greatwall.ui.screens

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scottyab.rootbeer.RootBeer
import ir.rabinn.greatwall.ui.components.AppItem
import ir.rabinn.greatwall.ui.components.AppListHeader
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onMenuClick: () -> Unit) {
    val context = LocalContext.current
    val pm = context.packageManager

    var showSystemApps by remember { mutableStateOf(false) }
    var isRooted by remember { mutableStateOf(false) }
    var isHotspotVpnEnabled by remember { mutableStateOf(false) }

    // چک کردن روت
    LaunchedEffect(Unit) {
        val rootBeer = RootBeer(context)
        isRooted = rootBeer.isRooted
    }

    // گرفتن لیست برنامه‌ها
    val apps = remember(showSystemApps) {
        pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter {
                showSystemApps || (it.flags and ApplicationInfo.FLAG_SYSTEM) == 0
            }
            .sortedBy { it.loadLabel(pm).toString().lowercase() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("GreatWall Firewall") },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
        ) {
            Text(
                "Root Status: ${if (isRooted) "Rooted 🔥" else "Not Rooted ❌"}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Enable VPN over Hotspot", fontSize = 14.sp)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = isHotspotVpnEnabled,
                    onCheckedChange = {
                        if (isRooted) {
                            isHotspotVpnEnabled = it
                            Toast.makeText(context, "VPN over Hotspot: $it", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Root access required", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.size(width = 36.dp, height = 20.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("Show System Apps", fontSize = 14.sp)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = showSystemApps,
                    onCheckedChange = { showSystemApps = it },
                    modifier = Modifier.size(width = 36.dp, height = 20.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            AppListHeader()

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(apps) { app ->
                    AppItem(appInfo = app, pm = pm)
                }
            }
        }
    }
}
