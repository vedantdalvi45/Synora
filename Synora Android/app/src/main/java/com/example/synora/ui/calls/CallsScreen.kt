package com.example.synora.ui.calls

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.synora.ui.components.SynoraEmptyState
import com.example.synora.ui.components.SynoraTopBar
import com.example.synora.ui.theme.SynoraTheme

@Composable
fun CallsScreen() {
    Scaffold(
        topBar = { SynoraTopBar(title = "Calls") }
    ) { innerPadding ->
        SynoraEmptyState(
            title = "No recent calls",
            description = "Your call history will appear here",
            modifier = Modifier.padding(innerPadding),
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Calls — Light")
@Composable
private fun CallsLightPreview() {
    SynoraTheme(darkTheme = false) { CallsScreen() }
}

@Preview(showBackground = true, name = "Calls — Dark")
@Composable
private fun CallsDarkPreview() {
    SynoraTheme(darkTheme = true) { CallsScreen() }
}
