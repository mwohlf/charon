package net.wohlfart.charon.service

import jakarta.mail.internet.MimeMessage
import mu.KotlinLogging
import net.wohlfart.charon.mail.MailBuilder
import net.wohlfart.charon.mail.MailTemplateRenderer
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets

private val logger = KotlinLogging.logger {}

@Service
class SendmailService(
    private val mailSender: JavaMailSenderImpl,
    private val mailTemplateRenderer : MailTemplateRenderer,
) {
    fun sendEmail(mailBuilder: MailBuilder) {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val mail = mailBuilder.build()
        val output = mailTemplateRenderer.render(mail)

        // mailAttributes like subject, receiver etc. are defined in a template macro and set during templateEngine.render as a side effect
        val mimeMessageHelper = MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name())
        mimeMessageHelper.setSubject(mail.mailSubject!!)
        mimeMessageHelper.setFrom(mail.mailFrom!!)
        mimeMessageHelper.setReplyTo(mail.mailReplyTo!!)
        mimeMessageHelper.setTo(mail.mailTo.toTypedArray())
        mimeMessageHelper.setText(output, true)
        val message: MimeMessage = mimeMessageHelper.mimeMessage
        logger.info { "sending $message" }
        mailSender.send(message)
    }
}
