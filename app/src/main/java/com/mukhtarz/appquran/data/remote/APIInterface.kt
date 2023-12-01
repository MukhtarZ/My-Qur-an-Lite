package com.mukhtarz.appquran.data.remote

import com.mukhtarz.appquran.data.remote.model.list.JadwalSholatList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

interface APIInterface {

    @GET("day")
    suspend fun getJadwal(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ):JadwalSholatList

    companion object {
        private const val BASE_URL = "https://prayer-times-xi.vercel.app/api/prayer/"

        fun createApi(): APIInterface {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIInterface::class.java)
        }
    }

}