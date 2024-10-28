package com.apps.numbersinfoapp.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NumbersApi {
    @GET("{num}")
    fun getNumberInfo(@Path("num") num: String): Call<String>

    @GET("random/math")
    fun getRandom(): Call<String>
}