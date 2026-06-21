package com.example.utilityapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.utilityapp.screens.ComingSoonScreen
import com.example.utilityapp.screens.CurrencyConverterScreen
import com.example.utilityapp.screens.UtilityScreen
import com.example.utilityapp.ui.theme.UtilityAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UtilityAppTheme {
                UtilityApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UtilityAppPreview() {
    UtilityAppTheme {
        UtilityApp()
    }
}

@Composable
fun UtilityApp() {
    var selectedTab by remember { mutableStateOf("Home") }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            MetroNavBar(
                selectedTab   = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                "Home"              -> UtilityScreen(onNavigate = { selectedTab = it })
                "CurrencyConverter" -> CurrencyConverterScreen(onBack = { selectedTab = "Home" })
                "ComingSoon"        -> ComingSoonScreen(onBack = { selectedTab = "Home" })
            }
        }
    }
}

@Composable
fun MetroNavBar(
    selectedTab: String,
    onTabSelected: (String) -> Unit
)
//Bottom Navbar To Choose Screen
{
    NavigationBar(
        containerColor = Color(0xFF1F1F1F),
        contentColor   = Color.White
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (selectedTab == "Home") Color(0xFF99FFFF) else Color.White
                )
            },
            label = {
                Text(
                    "home",
                    color = if (selectedTab == "Home") Color(0xFF99FFFF) else Color.White
                )
            },
            selected = selectedTab == "Home",
            onClick  = { onTabSelected("Home") },
            colors   = androidx.compose.material3.NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFF0078D4)
            )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Coming Soon",
                    tint = if (selectedTab == "ComingSoon") Color(0xFF99FFFF) else Color.White
                )
            },
            label = {
                Text(
                    "coming soon",
                    color = if (selectedTab == "ComingSoon") Color(0xFF99FFFF) else Color.White
                )
            },
            selected = selectedTab == "ComingSoon",
            onClick  = { onTabSelected("ComingSoon") },
            colors   = androidx.compose.material3.NavigationBarItemDefaults.colors(
                indicatorColor = Color(0xFF0078D4)
            )
        )
    }
}