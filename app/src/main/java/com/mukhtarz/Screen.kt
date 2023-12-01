package com.mukhtarz

sealed class Screen(val route : String) {

    object Home: Screen("home")

    object Read : Screen("read?&surahNumber={surahNumber}&juzNumber={juzNumber}&page={page}&index={index}"){
        fun createRoute(
            surahNumber : Int?,
            juzNumber : Int?,
            page : Int?,
            index : Int?
        ) : String {
            return "read?&surahNumber=${surahNumber}&juzNumber=${juzNumber}&page=${page}&index=${index}"
        }
    }

    object Time : Screen ("jadwalSholat")

    object Quran : Screen ("quran")

    object Bookmarks : Screen ("bookmarks")

    object OnBoarding : Screen ("screenAwal")


}
