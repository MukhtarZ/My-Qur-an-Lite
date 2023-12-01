@file:OptIn(ExperimentalFoundationApi::class)

package com.mukhtarz.appquran

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mukhtarz.Screen
import com.mukhtarz.appquran.data.GlobalState
import com.mukhtarz.appquran.data.bottomNavList
import com.mukhtarz.appquran.data.kotpref.LastReadPref
import com.mukhtarz.appquran.onboard.ScreenAwals
import com.mukhtarz.appquran.onboard.ScreenKeduas
import com.mukhtarz.appquran.onboard.ScreenKeempat
import com.mukhtarz.appquran.onboard.ScreenKetiga
import com.mukhtarz.appquran.screen.BacaSurahScreen
import com.mukhtarz.appquran.screen.BookmarkScreen
import com.mukhtarz.appquran.screen.CariKiblatScreen
import com.mukhtarz.appquran.screen.JadwalSholatScreen
import com.mukhtarz.appquran.screen.SettingScreen
import com.mukhtarz.appquran.screen.HomeScreen
import com.mukhtarz.appquran.screen.TabsJuz
import com.mukhtarz.appquran.ui.theme.AppQuranTheme
import kotlinx.android.parcel.Parcelize

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            AppQuranTheme(
                useDarkTheme = GlobalState.DarkMode
            ) {

                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController: NavHostController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    Scaffold(
                        modifier = Modifier,
                        bottomBar = {
                            if (bottomNavList.any { it.route == currentRoute })
                                NavigationBar {
                                    bottomNavList.map { item ->
                                        NavigationBarItem(
                                            selected = currentRoute == item.route,
                                            onClick = {
                                                navController.navigate(item.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    restoreState = true
                                                    launchSingleTop = true
                                                }
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector = item.icon,
                                                    contentDescription = item.label
                                                )
                                            },
                                            label = {
                                                Text(text = item.label)
                                            }
                                        )

                                    }
                                }
                        }
                    ) { innerPadding ->
                        NavHost(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                            startDestination =
                            if (!GlobalState.onBoarding){
                                Screen.Home.route
                            }else{
                                Screen.OnBoarding.route
                            }
                        ) {



                            composable(Screen.Quran.route) {
                                TabsJuz(
                                    goToRead = { surahNumber, juzNumber, page, index ->
                                        navController.navigate(
                                            Screen.Read.createRoute(
                                                surahNumber = surahNumber,
                                                juzNumber = juzNumber,
                                                page = page,
                                                index = index
                                            )

                                        )
                                    }
                                )
                            }
                            composable(Screen.Time.route) {
                                JadwalSholatScreen(
                                    goToHome = {
                                        navController.navigate(Screen.Home.route)
                                    }
                                )
                            }
                            composable(Screen.Bookmarks.route) {
                                BookmarkScreen(goToHome = {
//                                    navController.navigateUp()
                                    navController.navigate(Screen.Home.route)
                                }, goToRead = {surahNumber, juzNumber, page, index ->
                                    navController.navigate(
                                        Screen.Read.createRoute(
                                            surahNumber = surahNumber,
                                            juzNumber = null,
                                            page = null,
                                            index = index
                                        )
                                    )
                                })
                            }
                            composable(Screen.Home.route) {
                                HomeScreen(goToKiblat = {
                                    navController.navigate("screenKiblat")
                                },
                                    goToRead = {
                                        navController.navigate(
                                            Screen.Read.createRoute(
                                                surahNumber = LastReadPref.lastSurahNumber,
                                                null,null,
                                                index = LastReadPref.index
                                            )
                                        )
                                    }
                                )
                            }
                            composable(Screen.OnBoarding.route) {
                                ScreenAwals(goToScreenKeduas = { navController.navigate("screenKedua") },
                                    goToScreenKeempats = {navController.navigate("screenKeempat")})
                            }
                            composable("screenKedua") {
                                ScreenKeduas(
                                    goToScreenKetigas = { navController.navigate("screenKetiga") },
                                    goToScreenAwaals = { navController.navigate("screenAwal") })
                            }
                            composable("screenKetiga") {
                                ScreenKetiga(goToScreenKeempats = { navController.navigate("screenKeempat") },
                                    goToScreenKeduas = {navController.navigate("screenKedua")})
                            }
                            composable("screenKeempat") {
                                ScreenKeempat(
                                    goToScreenKetigas = { navController.navigate("screenKetiga") },
                                    goToHome = { navController.navigate(Screen.Home.route )})
                            }
                            composable("screenKiblat") {
                                CariKiblatScreen(goToHome = {navController.navigate(Screen.Home.route)})
                            }
                            composable(Screen.Read.route,
                                arguments = listOf(
                                    navArgument("surahNumber") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    },
                                    navArgument("juzNumber") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    },
                                    navArgument("page") {
                                        type = NavType.IntType
                                        defaultValue = -1
                                    },
                                    navArgument("index"){
                                        type = NavType.IntType
                                        defaultValue = -1
                                    }
                                )
                            ) {
                                val surahNumber = it.arguments?.getInt("surahNumber") ?: -1
                                val juzNumber = it.arguments?.getInt("juzNumber") ?: -1
                                val page = it.arguments?.getInt("page") ?: -1
                                val index = it.arguments?.getInt("index") ?: -1
                                Log.d("PAGE", page.toString())
                                Log.d("JUZ", juzNumber.toString())
                                Log.d("SURAH", surahNumber.toString())
                                Log.d("INDEX",index.toString())
                                BacaSurahScreen(
                                    surahNumber = surahNumber,
                                    juzNumber = juzNumber,
                                    page = page,
                                    index = index,
                                    goBack = {
                                        navController.navigateUp()
                                    })

                            }
                            composable("setting"){
                                SettingScreen(goHome = {
                                    navController.navigate(Screen.Home.route)
                                })
                            }


                        }
                    }
                }

            }
        }
    }
}


@Parcelize
data class JadwalSholat(
    val shalat: String,
    val waktu: String
) : Parcelable



