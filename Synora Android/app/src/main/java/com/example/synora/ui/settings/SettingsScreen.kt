package com.example.synora.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.synora.ui.components.SynoraTopBar
import com.example.synora.ui.theme.SynoraTheme

@Composable
fun SettingsScreen(onNavigateUp: () -> Unit) {
    Scaffold(
        topBar = { SynoraTopBar(title = "Settings", onNavigateUp = onNavigateUp) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Settings", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Theme, preferences — coming soon",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Settings — Light")
@Composable
private fun SettingsLightPreview() {
    SynoraTheme(darkTheme = false) { SettingsScreen(onNavigateUp = {}) }
}

@Preview(showBackground = true, name = "Settings — Dark")
@Composable
private fun SettingsDarkPreview() {
    SynoraTheme(darkTheme = true) { SettingsScreen(onNavigateUp = {}) }
}
