package com.sanisamoj

import com.sanisamoj.config.GlobalContext
import com.sanisamoj.data.models.dataclass.Operator
import com.sanisamoj.data.models.enums.OperatorStatus
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.data.models.interfaces.MailRepository
import com.sanisamoj.database.mongodb.Fields
import com.sanisamoj.database.mongodb.OperationField
import org.mindrot.jbcrypt.BCrypt

object ContextTest {
    private val databaseRepository: DatabaseRepository by lazy { GlobalContext.databaseRepository }
    val mailRepository: MailRepository by lazy { MailRepositoryTest() }

    val operator  = Operator(
        name = "OperatorTestName",
        email = "operatorEmail@domain.test",
        phone = "111111111111",
        password = "passwordTest"
    )

    lateinit var operatorTestInDb: Operator

    suspend fun createOperatorTest(operatorStatus: OperatorStatus = OperatorStatus.Disabled): Operator {
        val hashedPassword = BCrypt.hashpw(operator.password, BCrypt.gensalt())
        val operatorInDb: Operator = databaseRepository.createOperator(operator.copy(password = hashedPassword))
        databaseRepository.updateOperator(
            operatorId = operatorInDb.id.toString(),
            update = OperationField(Fields.Status, operatorStatus.name)
        )
        operatorTestInDb = operatorInDb
        return operatorInDb
    }

    suspend fun deleteOperator(operatorId: String = operator.id.toString()) {
        databaseRepository.deleteOperator(operatorId)
    }
}