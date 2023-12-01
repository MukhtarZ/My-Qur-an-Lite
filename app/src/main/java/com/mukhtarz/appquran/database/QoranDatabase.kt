package com.mukhtarz.appquran.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.DatabaseView
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mukhtarz.appquran.R


@Database(
    entities = [Qoran::class],
    views = [Surah::class, Juz::class, Page::class, SurahSearch::class, AyahSearch::class],
    version = 2,
    exportSchema = false
)
abstract  class QoranDatabase : RoomDatabase(){
    abstract fun dao() : QoranDao


    companion object{
        @Volatile private var INSTANCE: QoranDatabase? = null

        fun getInstance (context: Context): QoranDatabase {
            return INSTANCE?: synchronized(this){
                INSTANCE?: buildDatabase(context).also{
                    INSTANCE = it
                }
            }
        }

        private fun buildDatabase(context: Context): QoranDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                QoranDatabase::class.java,
                "qoran.db"
            ).createFromInputStream{
                context.resources.openRawResource(R.raw.qoran)
            }.fallbackToDestructiveMigration().build()
        }
    }
}

@DatabaseView("SELECT id,sora,sora_name_ar,sora_name_en,sora_name_id,COUNT(id) as ayah_total, sora_descend_place FROM quran GROUP by sora")
data class Surah(
    @PrimaryKey val id: Int? = 0,
    @ColumnInfo(name = "sora") val surahNumber: Int? = 0,
    @ColumnInfo(name = "sora_name_ar") val soraAr: String? ="",
    @ColumnInfo(name = "sora_name_en") val soraEn: String? ="",
    @ColumnInfo(name = "ayah_total") val ayahTotal: Int? = 0,
    @ColumnInfo(name = "sora_descend_place") val soraDescendPlace: String? = "",
    @ColumnInfo(name = "sora_name_id") val soraId: String? = "",
)
@DatabaseView("SELECT id,jozz,sora_name_ar,sora_name_en,sora_name_id,sora_descend_place,aya_no AS ayah_total From quran GROUP BY jozz")
data class Juz(
    @PrimaryKey val id: Int? = 0,
    @ColumnInfo(name = "jozz") val juzNumber: Int? = 0,
    @ColumnInfo(name = "sora") val surahNumber: Int? = 0,
    @ColumnInfo(name = "sora_name_ar") val soraAr: String? ="",
    @ColumnInfo(name = "sora_name_en") val soraEn: String? ="",
    @ColumnInfo(name = "ayah_total") val ayahTotal: Int? = 0,
    @ColumnInfo(name = "sora_descend_place") val soraDescendPlace: String? = "",
    @ColumnInfo(name = "sora_name_id") val soraId: String? = "",
)
@DatabaseView("SELECT id,page,sora_name_ar,sora_name_en,sora_name_id,COUNT(id) as ayah_total,page, sora_descend_place,sora_name_emlaey,aya_no FROM quran GROUP by page")
data class Page(
    @PrimaryKey val id: Int? = 0,
    @ColumnInfo(name = "page") val page: Int? = 0,
    @ColumnInfo(name = "aya_no") val ayaNo: Int? = 0,
    @ColumnInfo(name = "sora_name_ar") val soraAr: String? ="",
    @ColumnInfo(name = "sora_name_en") val soraEn: String? ="",
    @ColumnInfo(name = "ayah_total") val ayahTotal: Int? = 0,
    @ColumnInfo(name = "sora_descend_place") val soraDescendPlace: String? = "",
    @ColumnInfo(name = "sora_name_id") val soraId: String? = "",
    @ColumnInfo(name = "sora_name_emlaey") val soraNameEmlaey: String? = ""
)

//@DatabaseView("SELECT DISTINCT sora_name_en,sora_name_id,sora_name_ar,page from quran group by page")
//data class Halaman(
//    @PrimaryKey val id: Int? = 0,
//    @ColumnInfo(name = "sora_name_en") val soraEn: String? = "",
//    @ColumnInfo(name = "sora_name_id") val soraId: String? = "",
//    @ColumnInfo(name = "sora_name_ar") val soraAr :String? = "",
//    @ColumnInfo(name = "page") val page : Int? = 0
//)
@DatabaseView("SELECT id, sora_name_emlaey, COUNT(id) as ayah_total,jozz, sora_descend_place, sora from quran group by sora")
data class SurahSearch(
    @PrimaryKey val id: Int? = 0,
    @ColumnInfo(name = "sora_name_emlaey") val soraNameEmlaey: String? = "",
    @ColumnInfo(name = "ayah_total") val ayahTotal: Int? = 0,
    @ColumnInfo(name = "jozz") val juzNumber: Int? = 0,
    @ColumnInfo(name = "sora_descend_place") val soraDescendPlace: String? = "",
    @ColumnInfo(name = "sora") val surahNumber: Int? = 0
)
//@DatabaseView("SELECT id,aya_no,COUNT(id) as ayah_total, sora_name_emlaey,sora from quran group by sora")
//data class AyatSearch(
//    @PrimaryKey val id: Int? = 0,
//    @ColumnInfo(name = "aya_no") val ayaNo: Int? = 0,
//    @ColumnInfo(name = "ayah_total") val ayahTotal: Int? = 0,
//    @ColumnInfo(name = "sora") val surahNumber: Int? = 0,
//    @ColumnInfo(name = "sora_name_emlaey") val soraNameEmlaey: String? = ""
//)
@DatabaseView("SELECT id,aya_text_emlaey,sora_name_emlaey,sora,COUNT(id) as ayah_total, aya_no, aya_text,translation_id,translation_en from quran group by sora")
data class AyahSearch(
    @PrimaryKey val id: Int? = 0,
    @ColumnInfo(name = "aya_text_emlaey") val ayaTextBald: String? = "",
    @ColumnInfo(name = "ayah_total") val ayahTotal: Int? = 0,
    @ColumnInfo(name = "sora") val surahNumber: Int? = 0,
    @ColumnInfo(name = "aya_no") val ayaNo: Int? = 0,
    @ColumnInfo(name = "sora_name_emlaey") val soraNameEmlaey: String? = "",
    @ColumnInfo(name = "aya_text") val ayaText: String? = "",
    @ColumnInfo(name = "translation_en") val translationEn: String? = "",
    @ColumnInfo(name = "translation_id") val translationId: String? = ""

)
