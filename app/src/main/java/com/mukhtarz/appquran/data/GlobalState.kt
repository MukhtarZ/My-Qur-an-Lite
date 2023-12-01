package com.mukhtarz.appquran.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mukhtarz.appquran.data.kotpref.SetPref

object GlobalState {
    var DarkMode by mutableStateOf(SetPref.selectedTheme)
    var FocusMode by mutableStateOf(SetPref.isFocusRead)
    var Language by mutableStateOf(SetPref.selectedLanguage)
    var SelectedQari by mutableStateOf(SetPref.changeQaries)
    var onBoarding by mutableStateOf(SetPref.isOnBoarding)
}