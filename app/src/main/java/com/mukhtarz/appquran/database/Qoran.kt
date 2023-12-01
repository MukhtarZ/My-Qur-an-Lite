package com.mukhtarz.appquran.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran")
data class Qoran(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "jozz") val juzNumber: Int? = 0,
    @ColumnInfo(name = "sora") val surahNumber: Int? = 0,
    @ColumnInfo(name = "sora_name_en") val soraEn: String? = "",
    @ColumnInfo(name = "sora_name_ar") val soraAr: String? = "",
    @ColumnInfo(name = "page") val page: Int? = 0,
    @ColumnInfo(name = "aya_no") val ayaNo: Int? = 0,
    @ColumnInfo(name = "aya_text") val ayaText: String? = "",
    @ColumnInfo(name = "aya_text_emlaey") val ayaTextBald: String? = "",
    @ColumnInfo(name = "translation_id") val translationId: String? = "",
    @ColumnInfo(name = "footnotes_id") val footnotesId: String? = "",
    @ColumnInfo(name = "sora_name_id") val soraNameId: String? = "",
    @ColumnInfo(name = "sora_descend_place") val soraDescendPlace: String? = "",
    @ColumnInfo(name = "sora_name_emlaey") val soraNameEmlaey: String? = "",
    @ColumnInfo(name = "translation_en") val translationEn: String? = "",
    @ColumnInfo(name = "footnotes_en") val footnotesEn: String? = "",
)
