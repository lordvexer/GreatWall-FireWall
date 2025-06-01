package ir.rabinn.greatwall.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun drawerContent(
    currentScreen: String,
    onDestinationClicked: (String) -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        Text("GreatWall Menu", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)

        NavigationDrawerItem(
            label = { Text("Home") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            selected = currentScreen == "home",
            onClick = { onDestinationClicked("home") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Donate") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            selected = currentScreen == "donate",
            onClick = { onDestinationClicked("donate") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("About") },
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            selected = currentScreen == "about",
            onClick = { onDestinationClicked("about") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
