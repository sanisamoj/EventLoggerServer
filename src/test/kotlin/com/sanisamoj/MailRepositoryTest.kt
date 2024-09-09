package com.sanisamoj

import com.sanisamoj.data.models.dataclass.SendEmailData
import com.sanisamoj.data.models.interfaces.MailRepository

class MailRepositoryTest: MailRepository {
    override fun sendEmail(sendEmailData: SendEmailData) {
        println(sendEmailData)
    }
}