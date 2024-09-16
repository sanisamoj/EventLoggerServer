package com.sanisamoj.config

import com.sanisamoj.data.models.interfaces.BotRepository
import com.sanisamoj.database.mongodb.MongoDatabase
import com.sanisamoj.utils.schedule.ScheduleRoutine
import com.sanisamoj.utils.schedule.models.JobIdentification
import com.sanisamoj.utils.schedule.models.RoutineGroups
import com.sanisamoj.utils.schedule.models.StartRoutineData
import com.sanisamoj.utils.schedule.routines.UpdateBotApiToken
import org.quartz.JobKey

object Config {
    private val jobsIdentificationList: MutableList<JobIdentification> = mutableListOf()
    private const val EVERY_TWO_YEARS_CRON: String = "0 0 0 1 1 ? */2"

    suspend fun initialize() {
        databaseInitialize()
        botApiInitialize()
        updateBotApiToken()
    }

    private suspend fun databaseInitialize() {
        MongoDatabase.initialize()
    }

    private suspend fun botApiInitialize() {
        val botRepository: BotRepository = GlobalContext.botRepository
        botRepository.updateToken()
    }

    private fun updateBotApiToken() {
        val routineName = "UpdateBotApiToken-EveryTwoYears_At00"
        val startRoutineData = StartRoutineData(
            name = routineName,
            group = RoutineGroups.TokenUpdate,
            cronExpression = EVERY_TWO_YEARS_CRON
        )

        val jobKey: JobKey = ScheduleRoutine().startRoutine<UpdateBotApiToken>(startRoutineData)
        val jobIdentification = JobIdentification(jobKey, routineName)
        jobsIdentificationList.add(jobIdentification)
    }

}