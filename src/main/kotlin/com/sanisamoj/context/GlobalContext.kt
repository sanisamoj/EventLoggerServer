package com.sanisamoj.context

import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.data.models.interfaces.SessionRepository
import com.sanisamoj.data.models.language.LanguageResource
import com.sanisamoj.data.repository.DatabaseDefaultRepository
import com.sanisamoj.data.repository.SessionDefaultRepository
import java.util.concurrent.TimeUnit

object GlobalContext {
    const val EMPTY_VALUE: String = ""
    const val EMPTY_VALIDATION_CODE: Int = -1
    val VERSION: String = "0.2.1"

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

    val databaseRepository: DatabaseRepository = DatabaseDefaultRepository()
    val sessionRepository: SessionRepository = SessionDefaultRepository()

    val errorMessages = LanguageResource().errorMessages
    val actionMessages =  LanguageResource().actionMessages
    val infoMessages = LanguageResource().infoMessages
    val testMessages = LanguageResource().testMessages
    val warningMessages = LanguageResource().warningMessages

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