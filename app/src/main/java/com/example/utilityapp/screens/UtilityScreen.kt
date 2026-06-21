package com.example.utilityapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UtilityScreen(onNavigate: (String) -> Unit) {

    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000L)
        }
    }

    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())

    val timeString = timeFormat.format(currentTime)
    val dateString = dateFormat.format(currentTime).lowercase()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {
        // Status bar
        Text(
            text     = "hi nawwal",
            color    = Color.White,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 24.dp, top = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time
        Text(
            text       = timeString,
            color      = Color.White,
            fontSize   = 64.sp,
            fontWeight = FontWeight.Light,
            modifier   = Modifier.padding(start = 24.dp)
        )

        // Date
        Text(
            text     = dateString,
            color    = Color(0xFF99FFFF),
            fontSize = 18.sp,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(start = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tiles grid — 2-column square grid
        Column(
            modifier            = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetroTile(
                    label    = "currency converter",
                    color    = Color(0xFF0078D4),
                    modifier = Modifier.weight(1f),
                    onClick  = { onNavigate("CurrencyConverter") }
                )
                MetroTile(
                    label    = "unit converter",
                    color    = Color(0xFF0178D3),
                    modifier = Modifier.weight(1f),
                    onClick  = { onNavigate("ComingSoon") }
                )
            }

            // Row 2
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetroTile(
                    label    = "todo list",
                    color    = Color(0xFF1F1F1F),
                    modifier = Modifier.weight(1f),
                    onClick  = { onNavigate("ComingSoon") }
                )
                MetroTile(
                    label    = "weather",
                    color    = Color(0xFF0178D3),
                    modifier = Modifier.weight(1f),
                    onClick  = { onNavigate("ComingSoon") }
                )
            }

            // Row 3 — one tile + spacer keeps it square (half width)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetroTile(
                    label    = "passwords",
                    color    = Color(0xFF1F1F1F),
                    modifier = Modifier.weight(1f),
                    onClick  = { onNavigate("ComingSoon") }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun MetroTile(
    label    : String,
    color    : Color,
    modifier : Modifier = Modifier,
    onClick  : () -> Unit
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(color)
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            text     = label,
            color    = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}