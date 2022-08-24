package com.softprodigy.ballerapp.ui.features.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.softprodigy.ballerapp.R
import com.softprodigy.ballerapp.common.Route.EVENTS_SCREEN
import com.softprodigy.ballerapp.common.Route.TEAMS_SCREEN
import com.softprodigy.ballerapp.ui.features.components.TabBar
import com.softprodigy.ballerapp.ui.features.user_type.BottomNavigationBar
import com.softprodigy.ballerapp.ui.theme.BallerAppTheme
import com.softprodigy.ballerapp.ui.theme.appColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            BallerAppTheme {
                val navController = rememberNavController()
                Surface(
                    color = MaterialTheme.appColors.material.primary
                ) {
                    // Scaffold Component
                    Scaffold(
                        topBar = {
                            TabBar(color = MaterialTheme.appColors.material.primaryVariant) {
                                Text(
                                    text = "EVENTS",
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.align(Alignment.Center),
                                    color = Color.White
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_settings),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(all = dimensionResource(id = R.dimen.size_16dp)),
                                    tint = Color.White
                                )
                            }
                        },
                        content = {
                            NavControllerComposable(navController = navController)
                        },
                        bottomBar = {
                            BottomNavigationBar(navController = navController)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun NavControllerComposable(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = EVENTS_SCREEN) {
        composable(route = EVENTS_SCREEN) {
            HomeScreen(name = "")
        }
        composable(route = TEAMS_SCREEN) {
            HomeScreen(name = "")
        }
    }
}