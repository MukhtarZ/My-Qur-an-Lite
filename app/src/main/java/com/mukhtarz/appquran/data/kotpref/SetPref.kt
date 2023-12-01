package com.mukhtarz.appquran.data.kotpref

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumOrdinalPref

object SetPref: KotprefModel() {

    const val INDONESIAN = 0
    const val ENGLISH = 1
    const val LIGHT_MODE = false
    const val DARK_MODE = true
    const val DEFAULT_MODE = false
    const val FOCUS_MODE = true


    var selectedTheme by booleanPref(LIGHT_MODE)

    var selectedLanguage by intPref(INDONESIAN)

    var isFocusRead by booleanPref(DEFAULT_MODE)

    var changeQaries by enumOrdinalPref(Qaries.ALAFASY)

    var isOnBoarding by booleanPref(true)

}

enum class Qaries (val id:String, val qariName:String,val image: String){
    ALAFASY("Alafasy_128kbps","Alafasy","https://play-lh.googleusercontent.com/rRziKjkIzJqihgCof-cA_H3v6iHUdohUyN5J0iJ1hBA_gnbsuI-A_1i_syM5D9pc0w"),
    GHAMADI("Ghamadi_40kbps","Ghamadi","https://i.scdn.co/image/ab67616d0000b273add14160cbb705631c9756d5"),
    ABDURRAHMAN_ASSUDAIS("Abdurrahmaan_As-Sudais_192kbps","Abdurrahman_As-Sudais","https://upload.wikimedia.org/wikipedia/commons/1/18/Abdul-Rahman_Al-Sudais_%28Cropped%2C_2011%29.jpg"),
    ABU_BAKR_ASHSHATREE("Abu_Bakr_Ash-Shaatree_128kbps","Abu_Bakr Ash-Shatree","https://static.qurancdn.com/images/reciters/3/abu-bakr-al-shatri-pofile.jpeg?v=1"),
    PARGHIZAR("Parhizgar_48kbps","Parghizar","https://i1.sndcdn.com/artworks-000044325674-oqhift-t500x500.jpg"),
    HANI_RIFAI("Hani_Rifai_192kbps","Hani-Rifai","https://lastfm.freetls.fastly.net/i/u/300x300/4d9e51962b2daa4e0c0b2327abc86426.jpg"),
    NABIL_RIFA3I("Nabil_Rifa3i_48kbps","Nabil_Rifa3i","https://islamicbulletin.org/?reciters=nabil-ar-rifai")


}