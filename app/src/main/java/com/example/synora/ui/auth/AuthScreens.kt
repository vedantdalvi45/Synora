package com.example.synora.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.synora.ui.theme.SynoraTheme

@Composable
fun LoginScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Phase 3 — Auth",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun RegisterScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Register", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Phase 3 — Auth",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Login — Light")
@Composable
private fun LoginLightPreview() {
    SynoraTheme(darkTheme = false) { LoginScreen() }
}

@Preview(showBackground = true, name = "Login — Dark")
@Composable
private fun LoginDarkPreview() {
    SynoraTheme(darkTheme = true) { LoginScreen() }
}

@Preview(showBackground = true, name = "Register — Light")
@Composable
private fun RegisterLightPreview() {
    SynoraTheme(darkTheme = false) { RegisterScreen() }
}

@Preview(showBackground = true, name = "Register — Dark")
@Composable
private fun RegisterDarkPreview() {
    SynoraTheme(darkTheme = true) { RegisterScreen() }
}
