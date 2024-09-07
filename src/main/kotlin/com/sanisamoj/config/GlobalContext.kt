package com.sanisamoj.config

import com.sanisamoj.api.BotApi
import com.sanisamoj.data.models.dataclass.WarningMessages
import com.sanisamoj.data.models.interfaces.BotRepository
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.data.models.interfaces.SessionRepository
import com.sanisamoj.data.models.language.LanguageResource
import com.sanisamoj.data.repository.DefaultBotRepository
import com.sanisamoj.data.repository.DefaultRepository
import com.sanisamoj.data.repository.SessionDefaultRepository
import com.sanisamoj.utils.analyzers.ResourceLoader
import java.util.concurrent.TimeUnit

object GlobalContext {
    const val EMPTY_VALUE: String = ""
    const val EMPTY_VALIDATION_CODE: Int = -1
    const val VERSION: String = "0.3.0"

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

    val botRepository: BotRepository by lazy { DefaultBotRepository(botApiService = BotApi.botApiService) }
    val databaseRepository: DatabaseRepository by lazy { DefaultRepository() }
    val sessionRepository: SessionRepository by lazy { SessionDefaultRepository() }

    val errorMessages = LanguageResource().errorMessages
    val actionMessages =  LanguageResource().actionMessages

    val warningMessagesToChat: WarningMessages = ResourceLoader.convertJsonInputStreamAsObject<WarningMessages>("/lang/en.json")

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