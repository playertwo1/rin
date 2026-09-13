package com.playertwo1.rin.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.playertwo1.rin.R

enum class RinDestination(
    val route: String,
    @StringRes val labelResId: Int,
    val icon: ImageVector
) {
    HOME("home", R.string.nav_home, Icons.Default.Home),
    PROJECTS("projects", R.string.nav_projects, Icons.Default.Folder),
    WORKSTATION("workstation", R.string.nav_workstation, Icons.Default.Devices),
    SETTINGS("settings", R.string.nav_settings, Icons.Default.Settings);

    companion object {
        val startDestination = HOME
    }
}
