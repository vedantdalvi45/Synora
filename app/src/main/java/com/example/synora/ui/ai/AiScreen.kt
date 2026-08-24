package com.example.synora.ui.ai

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.synora.ui.components.SynoraEmptyState
import com.example.synora.ui.components.SynoraTopBar
import com.example.synora.ui.theme.SynoraTheme

@Composable
fun AiScreen() {
    Scaffold(
        topBar = { SynoraTopBar(title = "AI Agents") }
    ) { innerPadding ->
        SynoraEmptyState(
            title = "AI Agents",
            description = "AI-powered agents will be available here",
            modifier = Modifier.padding(innerPadding),
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "AI — Light")
@Composable
private fun AiLightPreview() {
    SynoraTheme(darkTheme = false) { AiScreen() }
}

@Preview(showBackground = true, name = "AI — Dark")
@Composable
private fun AiDarkPreview() {
    SynoraTheme(darkTheme = true) { AiScreen() }
}
