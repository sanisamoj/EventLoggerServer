package com.sanisamoj.api

import com.sanisamoj.utils.analyzers.dotEnv
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BotApi {
    private val BOTS_BASE_URL = dotEnv("BOT_URL")

    private val retrofitBotsApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BOTS_BASE_URL)
        .build()

    val botApiService : BotApiService by lazy {
        retrofitBotsApi.create(BotApiService::class.java)
    }
}