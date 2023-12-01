package com.mukhtarz.appquran.data.kotpref

import com.chibatching.kotpref.KotprefModel

object LastReadPref: KotprefModel() {
    var lastSurahNumber by intPref(-1)
    var lastSurahName by stringPref("")
    var lastAyaNo by intPref(-1)
    var lastReadTime by longPref(-1L)
    var index by intPref(-1)
}