package ir.rabinn.greatwall.ui.screens

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.rabinn.greatwall.ui.components.AppItem
import com.scottyab.rootbeer.RootBeer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onMenuClick: () -> Unit) {
    val context = LocalContext.current
    val pm = context.packageManager

    var showSystemApps by remember { mutableStateOf(false) }
    var isRooted by remember { mutableStateOf(false) }
    var isHotspotVpnEnabled by remember { mutableStateOf(false) }

    // Search text state
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    // چک روت
    LaunchedEffect(Unit) {
        val rootBeer = RootBeer(context)
        isRooted = rootBeer.isRooted
    }

    // گرفتن لیست اپ‌ها و فیلتر بر اساس نمایش سیستم یا غیرسیستم و سرچ
    val apps = remember(showSystemApps, searchQuery.text) {
        pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { app ->
                val isSystemApp = (app.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                val matchesSystemFilter = if (showSystemApps) true else !isSystemApp
                val label = app.loadLabel(pm).toString().lowercase()
                val matchesSearch = searchQuery.text.lowercase() in label
                matchesSystemFilter && matchesSearch
            }
            .sortedBy { it.loadLabel(pm).toString().lowercase() }
    }

    // ذخیره وضعیت وای‌فای و دیتا برای هر اپ (با کلید packageName)
    val wifiStates = remember { mutableStateMapOf<String, Boolean>() }
    val dataStates = remember { mutableStateMapOf<String, Boolean>() }

    // مقداردهی اولیه برای اپ‌ها (اگر نبود)
    apps.forEach { app ->
        if (!wifiStates.containsKey(app.packageName)) wifiStates[app.packageName] = true
        if (!dataStates.containsKey(app.packageName)) dataStates[app.packageName] = true
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
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enable VPN over Hotspot", fontSize = 14.sp)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = isHotspotVpnEnabled,
                    onCheckedChange = {
                        if (isRooted) {
                            isHotspotVpnEnabled = it
                        } else {
                            // اینجا می‌تونی پیام خطا یا Toast بزنی که روت نیست
                        }
                    },
                    modifier = Modifier.size(width = 36.dp, height = 20.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show System Apps", fontSize = 14.sp)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = showSystemApps,
                    onCheckedChange = { showSystemApps = it },
                    modifier = Modifier.size(width = 36.dp, height = 20.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            // فیلد جستجو
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search apps") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f)) // جای آیکون و نام برنامه

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Wi-Fi", fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold, modifier = Modifier.width(40.dp))
                    Text("Cell", fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold, modifier = Modifier.width(40.dp))
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(apps) { app ->
                    AppItem(
                        appInfo = app,
                        pm = pm,
                        wifiEnabled = wifiStates[app.packageName] ?: true,
                        mobileEnabled = dataStates[app.packageName] ?: true,
                        onWifiToggle = { enabled -> wifiStates[app.packageName] = enabled },
                        onMobileToggle = { enabled -> dataStates[app.packageName] = enabled }
                    )
                }
            }
        }
    }
}
