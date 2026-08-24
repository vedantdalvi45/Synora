package com.example.synora.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.synora.ui.ai.AiScreen
import com.example.synora.ui.auth.LoginScreen
import com.example.synora.ui.auth.RegisterScreen
import com.example.synora.ui.calls.CallsScreen
import com.example.synora.ui.chat.ChatScreen
import com.example.synora.ui.chat.ChatsScreen
import com.example.synora.ui.home.ActivityScreen
import com.example.synora.ui.profile.ProfileScreen
import com.example.synora.ui.settings.SettingsScreen
import com.example.synora.ui.splash.SplashScreen

private val bottomNavRoutes = bottomNavItems.map { it.destination.route }.toSet()

// Routes that manage their own insets (full-screen, no scaffold padding needed)
private val fullScreenRoutes = setOf(NavDestination.ChatConversation.route)

@Composable
fun SynoraApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.route in bottomNavRoutes
    val isFullScreen = currentDestination?.route?.let { route ->
        fullScreenRoutes.any { route.startsWith(it.substringBefore("{")) }
    } == true

    Scaffold(
        // Full-screen routes handle their own insets — give scaffold zero insets
        contentWindowInsets = if (isFullScreen) WindowInsets(0) else WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy
                            ?.any { it.route == item.destination.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label,
                                )
                            },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavDestination.Splash.route,
            // Full-screen routes get no padding; all others get scaffold padding
            modifier = if (isFullScreen) Modifier else Modifier.padding(innerPadding),
        ) {
            composable(NavDestination.Splash.route) {
                SplashScreen(onSplashComplete = {
                    navController.navigate(NavDestination.Chats.route) {
                        popUpTo(NavDestination.Splash.route) { inclusive = true }
                    }
                })
            }
            composable(NavDestination.Login.route)    { LoginScreen() }
            composable(NavDestination.Register.route) { RegisterScreen() }
            composable(NavDestination.Chats.route) {
                ChatsScreen(onChatClick = { id, name ->
                    navController.navigate(NavDestination.ChatConversation.createRoute(id, name))
                })
            }
            composable(
                route = NavDestination.ChatConversation.route,
                arguments = listOf(
                    navArgument("contactId")   { type = NavType.IntType },
                    navArgument("contactName") { type = NavType.StringType },
                ),
            ) {
                // ChatScreen draws edge-to-edge, handles its own status/nav bar insets
                ChatScreen(onNavigateUp = { navController.navigateUp() })
            }
            composable(NavDestination.Calls.route)    { CallsScreen() }
            composable(NavDestination.Ai.route)       { AiScreen() }
            composable(NavDestination.Activity.route) { ActivityScreen() }
            composable(NavDestination.Profile.route) {
                ProfileScreen(onNavigateToSettings = {
                    navController.navigate(NavDestination.Settings.route)
                })
            }
            composable(NavDestination.Settings.route) {
                SettingsScreen(onNavigateUp = { navController.navigateUp() })
            }
        }
    }
}
