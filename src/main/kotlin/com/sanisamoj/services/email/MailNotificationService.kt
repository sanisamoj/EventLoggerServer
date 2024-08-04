package com.sanisamoj.services.email

import com.sanisamoj.context.MailContext
import com.sanisamoj.data.models.dataclass.SendEmailData
import com.sanisamoj.data.models.interfaces.MailRepository
import com.sanisamoj.data.repository.MailDefaultRepository
import com.sanisamoj.utils.analyzers.dotEnv

class MailNotificationService(
    private val mailRepository: MailRepository = MailDefaultRepository
) {
    fun sendConfirmationTokenEmail(name: String, token: String, to: String) {
        val selfUrl = dotEnv("SELF_URL")
        val activationLink = "$selfUrl/operator/activate?token=$token"

        val text = MailContext.buildConfirmationTokenMail(name, activationLink)
        val topic = "Ative sua conta!"
        val sendEmailData = SendEmailData(to, topic, text, true)

        mailRepository.sendEmail(sendEmailData)
    }
}