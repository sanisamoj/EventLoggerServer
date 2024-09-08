package com.sanisamoj.services

import com.sanisamoj.config.GlobalContext
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.data.models.interfaces.SessionRepository
import com.sanisamoj.utils.eraseAllDataToTests

class OperatorAuthenticationServiceTest {
    private val databaseRepository: DatabaseRepository = GlobalContext.databaseRepository
    private val sessionRepository: SessionRepository = GlobalContext.sessionRepository

    init {
        eraseAllDataToTests()
    }
}