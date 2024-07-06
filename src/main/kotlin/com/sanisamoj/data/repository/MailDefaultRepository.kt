package com.sanisamoj.data.repository

import com.sanisamoj.data.models.generics.SendEmailData
import com.sanisamoj.data.models.interfaces.MailRepository
import com.sanisamoj.utils.analyzers.dotEnv
import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object MailDefaultRepository : MailRepository {
    private val session: Session by lazy { session() }
    private val email: String = dotEnv("EMAIL_SYSTEM")
    private val password: String = dotEnv("EMAIL_PASSWORD")

    private val props = Properties().apply {
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.ssl.protocols", "TLSv1.2")
        put("mail.smtp.socketFactory.port", "465")
        put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        put("mail.smtp.auth", "true")
        put("mail.smtp.port", "465")
        put("mail.smtp.ssl.trust", "*")
    }

    override fun sendEmail(sendEmailData: SendEmailData) {
        try {
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(email))
                setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(sendEmailData.to)
                )
                subject = sendEmailData.topic
                if(sendEmailData.isHtml) {
                    setContent(sendEmailData.text, "text/html")
                } else {
                    setText(sendEmailData.text)
                }

            }

            Transport.send(message)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun session(): Session {
        val session = Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(email, password)
            }
        }) ?: throw Exception("Without Mail Session")

        return session
    }
}