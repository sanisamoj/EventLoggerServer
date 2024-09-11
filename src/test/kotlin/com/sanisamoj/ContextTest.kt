package com.sanisamoj

import com.sanisamoj.data.models.dataclass.CreateApplicationServiceRequest
import com.sanisamoj.data.models.dataclass.Operator
import com.sanisamoj.data.models.enums.OperatorStatus
import com.sanisamoj.data.models.interfaces.BotRepository
import com.sanisamoj.data.models.interfaces.DatabaseRepository
import com.sanisamoj.data.models.interfaces.MailRepository
import com.sanisamoj.database.mongodb.Fields
import com.sanisamoj.database.mongodb.OperationField
import com.sanisamoj.repository.BotRepositoryTest
import com.sanisamoj.repository.MailRepositoryTest
import com.sanisamoj.repository.RepositoryTest
import org.mindrot.jbcrypt.BCrypt

object ContextTest {
    val databaseRepository: DatabaseRepository by lazy { RepositoryTest(botRepository) }
    val mailRepository: MailRepository by lazy { MailRepositoryTest() }
    val botRepository: BotRepository by lazy { BotRepositoryTest() }

    val operator  = Operator(
        name = "OperatorTestName",
        email = "operatorEmail@domain.test",
        phone = "111111111111",
        password = "passwordTest"
    )

    val createApplicationService = CreateApplicationServiceRequest(
        applicationName = "ApplicationTest",
        description = "Application Test Description",
        password = "PasswordTest"
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