package com.sanisamoj.data.models.interfaces

import com.sanisamoj.data.models.generics.SendEmailData

interface MailRepository {
    fun sendEmail(sendEmailData: SendEmailData)
}