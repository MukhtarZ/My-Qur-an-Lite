package com.mukhtarz.appquran.data.remote.model.list


import com.google.gson.annotations.SerializedName

data class Time(
    @SerializedName("asr")
    val ashar: String?,
    @SerializedName("dhuha")
    val dhuha: String?,
    @SerializedName("dhuhr")
    val dhuhur: String?,
    @SerializedName("fajr")
    val subuh: String?,
    @SerializedName("gregorian")
    val gregorian: String?,
    @SerializedName("hijri")
    val hijri: String?,
    @SerializedName("imsak")
    val imsak: String?,
    @SerializedName("isha")
    val isya: String?,
    @SerializedName("maghrib")
    val maghrib: String?,
    @SerializedName("sunset")
    val sunset: String?,
    @SerializedName("timestamp")
    val timestamp: Long?
)