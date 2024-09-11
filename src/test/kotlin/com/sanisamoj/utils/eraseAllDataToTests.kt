package com.sanisamoj.utils

import com.sanisamoj.data.models.dataclass.LogEvent
import com.sanisamoj.data.models.dataclass.Operator
import com.sanisamoj.database.mongodb.CollectionsInDb
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun eraseAllDataToTests() {
    runBlocking {
        launch { eraseAllDataInMongodb<Operator>(CollectionsInDb.Operators) }
        launch { eraseAllDataInMongodb<LogEvent>(CollectionsInDb.LogEvent) }
        launch { eraseAllDataInMongodb<LogEvent>(CollectionsInDb.ApplicationServices) }
    }
}