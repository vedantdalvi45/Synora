package com.example.synora.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val destination: NavDestination,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem("Chats",    NavDestination.Chats,    Icons.Filled.Chat,          Icons.Outlined.Chat),
    BottomNavItem("Calls",    NavDestination.Calls,    Icons.Filled.Call,          Icons.Outlined.Call),
    BottomNavItem("AI",       NavDestination.Ai,       Icons.Filled.AutoAwesome,   Icons.Outlined.AutoAwesome),
    BottomNavItem("Activity", NavDestination.Activity, Icons.Filled.Notifications, Icons.Outlined.Notifications),
    BottomNavItem("Profile",  NavDestination.Profile,  Icons.Filled.Person,        Icons.Outlined.Person),
)
