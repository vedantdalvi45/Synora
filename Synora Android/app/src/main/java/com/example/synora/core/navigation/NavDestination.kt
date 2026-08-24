package com.example.synora.core.navigation

sealed class NavDestination(val route: String) {
    data object Splash           : NavDestination("splash")
    data object Login            : NavDestination("login")
    data object Register         : NavDestination("register")
    data object Chats            : NavDestination("chats")
    data object Calls            : NavDestination("calls")
    data object Ai               : NavDestination("ai")
    data object Activity         : NavDestination("activity")
    data object Profile          : NavDestination("profile")
    data object Settings         : NavDestination("settings")
    data object ChatConversation : NavDestination("chat/{contactId}/{contactName}") {
        fun createRoute(contactId: Int, contactName: String) =
            "chat/$contactId/${contactName.encodeForRoute()}"
    }
}

private fun String.encodeForRoute() = java.net.URLEncoder.encode(this, "UTF-8")
