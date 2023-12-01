package com.mukhtarz.appquran.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface QoranDao {
    @Query("SELECT * FROM quran WHERE sora = :surahNumber")
    fun getSurahBySora(surahNumber:Int): Flow<List<Qoran>>

    @Query("SELECT * FROM quran WHERE jozz = :juzNumber")
    fun getSurahByJozz(juzNumber:Int): Flow<List<Qoran>>

    @Query("SELECT * FROM quran WHERE page = :page")
    fun getSurahByPage(page:Int): Flow<List<Qoran>>

    @Query("SELECT * from surahsearch where sora_name_emlaey like '%' || :soraNameEmlaey || '%' ")
    fun getSurahBySearch(soraNameEmlaey:String): Flow<List<SurahSearch>>

//    @Query("SELECT * from ayatsearch where aya_no = :ayaNo")
//    fun getAyatBySearch(ayaNo:Int): Flow<List<AyatSearch>>

    @Query("SELECT * from ayahsearch where aya_text_emlaey like '%' || :ayaTextBald || '%' OR translation_en like '%' || :ayaTextBald || '%' OR translation_id like '%' || :ayaTextBald || '%'")
    fun getAyahBySearch(ayaTextBald:String): Flow<List<AyahSearch>>

    @Query("SELECT * FROM surah")
    fun listSurahBySurah(): Flow<List<Surah>>

    @Query("SELECT * FROM juz")
    fun listSurahByJuz(): Flow<List<Juz>>

    @Query("SELECT * FROM page")
    fun listSurahByPage(): Flow<List<Page>>
}