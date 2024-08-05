package com.sanisamoj.config

import com.sanisamoj.api.BotApi
import com.sanisamoj.data.models.interfaces.BotRepository
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.data.models.interfaces.SessionRepository
import com.sanisamoj.data.models.language.LanguageResource
import com.sanisamoj.data.repository.DefaultBotRepository
import com.sanisamoj.data.repository.DefaultRepository
import com.sanisamoj.data.repository.SessionDefaultRepository
import java.util.concurrent.TimeUnit

object GlobalContext {
    const val EMPTY_VALUE: String = ""
    const val EMPTY_VALIDATION_CODE: Int = -1
    const val VERSION: String = "0.2.3"

    var mobileMinVersion: String = "1.0.0"
        private set
    var mobileTargetVersion: String = "1.1.0"
        private set
    var desktopMinVersion: String = "1.1.0"
        private set
    var desktopTargetVersion: String = "1.1.1"
        private set

    const val MAX_FILE_SIZE: Double = 10.0 * 1024 * 1024 // 10MB

    val OPERATOR_TOKEN_EXPIRATION: Long = TimeUnit.DAYS.toMillis(60)
    val APPLICATION_SERVICE_TOKEN_EXPIRATION: Long = TimeUnit.DAYS.toMillis(90)

    val databaseRepository: DatabaseRepository = DefaultRepository()
    val sessionRepository: SessionRepository = SessionDefaultRepository()
    val botRepository: BotRepository = DefaultBotRepository(apiService = BotApi.botApiService)

    val errorMessages = LanguageResource().errorMessages
    val actionMessages =  LanguageResource().actionMessages

    fun setMinVersion(isMobile: Boolean = false, minVersion: String) {
        if(isMobile) {
            this.mobileMinVersion = minVersion
        } else { this.desktopMinVersion = minVersion }
    }

    fun setTargetVersion(isMobile: Boolean = false, newVersion: String) {
        if(isMobile) {
            this.mobileTargetVersion = newVersion
        } else { this.desktopTargetVersion = newVersion }
    }
}