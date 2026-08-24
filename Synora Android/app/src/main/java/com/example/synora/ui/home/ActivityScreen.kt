package com.example.synora.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.synora.ui.components.SynoraEmptyState
import com.example.synora.ui.components.SynoraTopBar
import com.example.synora.ui.theme.SynoraTheme

@Composable
fun ActivityScreen() {
    Scaffold(
        topBar = { SynoraTopBar(title = "Activity") }
    ) { innerPadding ->
        SynoraEmptyState(
            title = "No activity yet",
            description = "Notifications and activity will appear here",
            modifier = Modifier.padding(innerPadding),
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Activity — Light")
@Composable
private fun ActivityLightPreview() {
    SynoraTheme(darkTheme = false) { ActivityScreen() }
}

@Preview(showBackground = true, name = "Activity — Dark")
@Composable
private fun ActivityDarkPreview() {
    SynoraTheme(darkTheme = true) { ActivityScreen() }
}
