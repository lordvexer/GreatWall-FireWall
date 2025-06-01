package ir.rabinn.greatwall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import ir.rabinn.greatwall.ui.screens.AboutScreen
import ir.rabinn.greatwall.ui.screens.DonateScreen
import ir.rabinn.greatwall.ui.screens.HomeScreen
import ir.rabinn.greatwall.ui.components.drawerContent
import ir.rabinn.greatwall.ui.theme.GreatWallTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GreatWallTheme {
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var currentScreen by remember { mutableStateOf("home") }

                ModalNavigationDrawer(
                    drawerContent = {
                        drawerContent(currentScreen = currentScreen) { selected ->
                            currentScreen = selected
                            scope.launch { drawerState.close() }
                        }
                    },
                    drawerState = drawerState
                ) {
                    when (currentScreen) {
                        "home" -> HomeScreen { scope.launch { drawerState.open() } }
                        "donate" -> DonateScreen { currentScreen = "home" }
                        "about" -> AboutScreen { currentScreen = "home" }
                    }
                }
            }
        }
    }
}
