package com.sanisamoj

import com.sanisamoj.config.GlobalContext
import com.sanisamoj.data.models.dataclass.Operator
import com.sanisamoj.data.models.enums.OperatorStatus
import com.sanisamoj.data.models.interfaces.DatabaseRepository

object ContextTest {
    private val databaseRepository: DatabaseRepository by lazy { GlobalContext.databaseRepository }
    private val operator  = Operator(
        name = "OperatorTestName",
        email = "operatorEmail@domain.test",
        phone = "111111111111",
        password = "passwordTest"
    )

    lateinit var operatorTest: Operator

    suspend fun createOperatorTest(operatorStatus: OperatorStatus = OperatorStatus.Disabled): Operator {
        val operatorInDb: Operator = databaseRepository.createOperator(operator)
        operatorTest = operatorInDb
        return operatorInDb
    }

    suspend fun deleteOperator(operatorId: String = operator.id.toString()) {
        databaseRepository.deleteOperator(operatorId)
    }
}