package com.playertwo1.rin.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.playertwo1.rin.ui.screens.home.HomeScreen
import com.playertwo1.rin.ui.screens.projects.ProjectsScreen
import com.playertwo1.rin.ui.screens.settings.SettingsScreen
import com.playertwo1.rin.ui.screens.workstation.WorkstationScreen

@Composable
fun RinApp(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: RinDestination.startDestination.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                RinDestination.entries.forEach { destination ->
                    val selected = currentRoute == destination.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (currentRoute != destination.route) {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = stringResource(destination.labelResId)
                            )
                        },
                        label = {
                            Text(text = stringResource(destination.labelResId))
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RinDestination.startDestination.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(RinDestination.HOME.route) {
                HomeScreen()
            }
            composable(RinDestination.PROJECTS.route) {
                ProjectsScreen()
            }
            composable(RinDestination.WORKSTATION.route) {
                WorkstationScreen()
            }
            composable(RinDestination.SETTINGS.route) {
                SettingsScreen()
            }
        }
    }
}
