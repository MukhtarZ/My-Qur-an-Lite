package com.mukhtarz.appquran.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationData(
val label : String,
val icon : ImageVector,
val route :String
)

val bottomNavList = listOf(
    BottomNavigationData("Home",Icons.Default.Home,"home"),
    BottomNavigationData("Bookmarks",Icons.Default.Bookmarks,"bookmarks"),
    BottomNavigationData("Read Qur'an",Icons.Default.Mosque,"quran"),
    BottomNavigationData("Setting",Icons.Default.Settings,"setting")
)
