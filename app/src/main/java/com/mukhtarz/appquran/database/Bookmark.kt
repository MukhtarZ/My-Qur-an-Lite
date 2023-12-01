package com.mukhtarz.appquran.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Entity(tableName = "bookmark")
data class Bookmark(
    val surahName: String? = "",
    val ayahNumber: Int? = 0,
    val surahNumber : Int? = 0,
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val createdAt : Long = System.currentTimeMillis(),
    val totalAyat: Int? = 0
)