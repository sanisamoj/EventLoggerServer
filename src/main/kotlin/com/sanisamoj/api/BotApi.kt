package com.sanisamoj.api

import com.sanisamoj.data.models.interfaces.BotsApiService
import com.sanisamoj.utils.analyzers.dotEnv
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BotApi {
    private val BOTS_BASE_URL = dotEnv("BOTS_BASE_URL")

    private val retrofitBotsApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BOTS_BASE_URL)
        .build()

    val botApiService : BotsApiService by lazy {
        retrofitBotsApi.create(BotsApiService::class.java)
    }
}