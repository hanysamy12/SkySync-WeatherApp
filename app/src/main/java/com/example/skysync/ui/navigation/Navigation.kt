package com.example.skysync.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.skysync.R
import kotlinx.serialization.Serializable

data class NavigationItem(
    val title: String, val icon: Painter, val route: String
)

private const val TAG = "BottomNavigationBar"
@Serializable
sealed class ScreenRoute(val route: String) {
    @Serializable
    data class Home(val lat: Double? = null, val lon: Double? = null) : ScreenRoute("home/$lat?/$lon?")
    @Serializable
    object Favorite : ScreenRoute("favorite")
    @Serializable
    object Alerts : ScreenRoute("alerts")
    @Serializable
    object Settings : ScreenRoute("settings")
    @Serializable
    data class GoogleMap(val sourceRoute : Int) : ScreenRoute("googleMap")
    @Serializable
    data class FavoriteDetails( val lat: Double, val lon: Double) : ScreenRoute("favoriteDetails")

}

@Composable
fun BottomNavigationBar(navController: NavController,) {

    val navigationItems = listOf(
        NavigationItem(stringResource(R.string.home), painterResource(R.drawable.ic_home), ScreenRoute.Home().route),
        NavigationItem(
            stringResource(R.string.favorite), painterResource(R.drawable.ic_heart), ScreenRoute.Favorite.route
        ),
        NavigationItem(stringResource(R.string.alerts), painterResource(R.drawable.ic_bellring), ScreenRoute.Alerts.route),
        NavigationItem(
            stringResource(R.string.settings), painterResource(R.drawable.ic_settings), ScreenRoute.Settings.route
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val selectedNavigationIndex = remember(currentRoute) {
        navigationItems.indexOfFirst { item ->
            when {
                item.route == ScreenRoute.Home().route -> currentRoute?.startsWith("home") == true
                else -> currentRoute == item.route
            }
        }.takeIf { it >= 0 } ?: 0
    }
    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(selected = selectedNavigationIndex == index, onClick = {
                navController.navigate(item.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                    restoreState =true
                }
            }, icon = { Icon(painter = item.icon, contentDescription = item.title) }, label = {
                if (index == selectedNavigationIndex) {
                    Text(
                        item.title
                    )
                }
            })
        }
    }
}
