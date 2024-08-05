package com.sanisamoj.data.models.interfaces

import com.sanisamoj.data.models.dataclass.*
import retrofit2.http.*

interface BotsApiService {
    @POST("admin")
    suspend fun login(@Body botServiceLoginRequest: BotServiceLoginRequest): AuthenticationResponse

    @POST("bot")
    suspend fun createBot(@Body createBotRequest: CreateBotRequest, @Header("Authorization") token: String) : BotResponse

    @DELETE("bot/{id}")
    suspend fun deleteBot(@Path("id") id: String, @Header("Authorization") token: String)

    @POST("bot/{id}/message")
    suspend fun sendMessage(
        @Path("id") botId: String,
        @Body botSendMessageRequest: BotSendMessageRequest,
        @Header("Authorization") token: String
    )

    @GET("bot/{id}")
    suspend fun getBot(@Path("id") botId: String, @Header("Authorization") token: String): BotResponse
}