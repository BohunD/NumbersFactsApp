package com.apps.numbersinfoapp.data.retrofit

import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object ApiService {
    private const val BASE_URL = "http://numbersapi.com/"

    private fun getRetrofit(): Retrofit{
        val clientInterceptor = Interceptor{
                chain ->
            var request: Request = chain.request()
            val url: HttpUrl = request.url().newBuilder()
                .build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val client = OkHttpClient.Builder().addInterceptor(clientInterceptor).build()
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
    val api: NumbersApi = getRetrofit().create(NumbersApi::class.java)
}