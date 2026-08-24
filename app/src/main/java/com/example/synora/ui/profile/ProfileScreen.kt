package com.example.synora.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.synora.ui.components.SynoraAvatar
import com.example.synora.ui.components.SynoraOutlinedButton
import com.example.synora.ui.components.SynoraTopBar
import com.example.synora.ui.theme.Spacing
import com.example.synora.ui.theme.SynoraTheme

@Composable
fun ProfileScreen(onNavigateToSettings: () -> Unit) {
    Scaffold(
        topBar = {
            SynoraTopBar(
                title = "Profile",
                actions = {
                    SynoraOutlinedButton(text = "Settings", onClick = onNavigateToSettings)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(Spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            SynoraAvatar(displayName = "Synora User", size = Spacing.xxxl)
            Spacer(Modifier.height(Spacing.md))
            Text("Synora User", style = MaterialTheme.typography.titleLarge)
            Text(
                "user@synora.dev",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Profile — Light")
@Composable
private fun ProfileLightPreview() {
    SynoraTheme(darkTheme = false) { ProfileScreen(onNavigateToSettings = {}) }
}

@Preview(showBackground = true, name = "Profile — Dark")
@Composable
private fun ProfileDarkPreview() {
    SynoraTheme(darkTheme = true) { ProfileScreen(onNavigateToSettings = {}) }
}
