package com.pulseai.ledgerone.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController

object LedgerOneDestinations {
    const val DASHBOARD_ROUTE = "dashboard"
    const val INSIGHTS_ROUTE = "insights"
    const val PLANNER_ROUTE = "planner"
    const val BUDGET_ROUTE = "budget"
    const val PROFILE_ROUTE = "profile"
    const val SETTINGS_ROUTE = "settings"
    const val SECURITY_ROUTE = "security"
    const val LINKED_BANKS_ROUTE = "linked_banks"
}

data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val BottomNavItems = listOf(
    NavigationItem(LedgerOneDestinations.DASHBOARD_ROUTE, Icons.Outlined.Home, "Home"),
    NavigationItem(LedgerOneDestinations.PLANNER_ROUTE, Icons.Outlined.EventNote, "Planner"),
    NavigationItem(LedgerOneDestinations.SETTINGS_ROUTE, Icons.Outlined.Settings, "Settings")
)

val DrawerNavItems = listOf(
    NavigationItem(LedgerOneDestinations.DASHBOARD_ROUTE, Icons.Outlined.Dashboard, "Dashboard"),
    NavigationItem(LedgerOneDestinations.PROFILE_ROUTE, Icons.Outlined.Person, "Profile"),
    NavigationItem(LedgerOneDestinations.LINKED_BANKS_ROUTE, Icons.Outlined.AccountBalance, "Linked Banks"),
    NavigationItem(LedgerOneDestinations.SECURITY_ROUTE, Icons.Outlined.Security, "Security"),
    NavigationItem(LedgerOneDestinations.SETTINGS_ROUTE, Icons.Outlined.Settings, "Settings")
)

class NavigationActions(private val navController: NavHostController) {
    fun navigateTo(route: String) {
        navController.navigate(route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}
