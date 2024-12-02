package org.internship.kmp.martin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.internship.kmp.martin.core.domain.AppConstants
import org.internship.kmp.martin.views.AuthView
import org.internship.kmp.martin.views.FavoriteTracksView
import org.internship.kmp.martin.views.SearchTracksView
import org.internship.kmp.martin.views.UserProfileView

@Composable
fun App() {
    var selectedTab by remember { mutableStateOf(0) }

    val navController = rememberNavController()

    val tabs = listOf(
        TabItem("Search", Icons.Default.Search, { SearchTracksView() }),
        TabItem("Favorites", Icons.Default.Favorite, { FavoriteTracksView() }),
        TabItem("Profile", Icons.Default.Person, { UserProfileView(navController) })
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Box(modifier = Modifier.weight(1f)) {
            tabs[selectedTab].content()
        }
        BottomNavigation {
            tabs.forEachIndexed { index, tab ->
                BottomNavigationItem(
                    icon = { Icon(tab.icon, contentDescription = tab.title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color(AppConstants.Colors.SECONDARY_TEXT_GREY_HEX.toColorInt()),
                    alwaysShowLabel = false,
                    modifier = Modifier.background(
                        Color(AppConstants.Colors.PRIMARY_DARK_HEX.toColorInt())
                    )
                )
            }
        }
    }

}

data class TabItem(val title: String, val icon: ImageVector, val content: @Composable () -> Unit)



