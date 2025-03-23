package com.example.skysync.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.skysync.R
import androidx.compose.runtime.getValue
import kotlinx.serialization.Serializable

data class NavigationItem(
    val title: String, val icon: Painter, val route: String
)

@Serializable
sealed class ScreenRoute(val route: String) {
    object Home : ScreenRoute("home")
    object Favorite : ScreenRoute("favorite")
    object Alerts : ScreenRoute("alerts")
    object Settings : ScreenRoute("settings")
    @Serializable
    object GoogleMap : ScreenRoute("googleMap")

}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navigationItems = listOf(
        NavigationItem("Home", painterResource(R.drawable.ic_home), ScreenRoute.Home.route),
        NavigationItem(
            "Favorite", painterResource(R.drawable.ic_heart), ScreenRoute.Favorite.route
        ),
        NavigationItem("Alerts", painterResource(R.drawable.ic_bellring), ScreenRoute.Alerts.route),
        NavigationItem(
            "Settings", painterResource(R.drawable.ic_settings), ScreenRoute.Settings.route
        )
    )
    val selectedNavigationIndex = rememberSaveable { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        val index = navigationItems.indexOfFirst { it.route == currentRoute }
        if (index != -1) {
            selectedNavigationIndex.intValue = index
        }
    }
    NavigationBar(containerColor = colorResource(id = R.color.teal_700)) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(selected = selectedNavigationIndex.intValue == index, onClick = {
                selectedNavigationIndex.intValue = index
                navController.navigate(item.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }, icon = { Icon(painter = item.icon, contentDescription = item.title) }, label = {
                if (index == selectedNavigationIndex.intValue){
                Text( item.title
                    /*item.title, color = if (index == selectedNavigationIndex.intValue) Color.Black
                    else Color.Gray*/
                )}
            })
        }
    }
}
