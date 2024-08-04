package com.sanisamoj.security

import com.sanisamoj.data.models.dataclass.AccountInAnalysis
import com.sanisamoj.database.redis.CollectionsInRedis
import com.sanisamoj.database.redis.DataIdentificationRedis
import com.sanisamoj.database.redis.Redis
import com.sanisamoj.services.operator.OperatorService
import java.util.concurrent.TimeUnit

class AccountAccessGuard {
    private val timeToLive: Long = TimeUnit.HOURS.toMillis(24)
    private val maximumViolationAllowed: Int = 3

    suspend fun violated(email: String) {
        val identificationRedis = DataIdentificationRedis(CollectionsInRedis.RateLimit, email)
        val accountInAnalysis = Redis.getObject<AccountInAnalysis>(identificationRedis)
        lateinit var accountToBeSave: AccountInAnalysis

        if (accountInAnalysis == null) {
            accountToBeSave = AccountInAnalysis(email, 1)
            return Redis.setObjectWithTimeToLive(identificationRedis, accountToBeSave, timeToLive)
        }

        if(accountInAnalysis.quantity >= maximumViolationAllowed) {
            OperatorService().blockOperatorByEmail(email)
        }

        val newQuantity = accountInAnalysis.quantity.inc()
        accountToBeSave = accountInAnalysis.copy(quantity = newQuantity)

        Redis.setObjectWithTimeToLive(identificationRedis, accountToBeSave, timeToLive)
    }
}