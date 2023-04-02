package net.wohlfart.charon.service

import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets


@Service
class SendmailService(
    private val mailSender: JavaMailSenderImpl,
) {
    fun sendEmail() {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        // mailAttributes like subject, receiver etc. are defined in a template macro and set during templateEngine.render as a side effect
        val mimeMessageHelper = MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name())
        mimeMessageHelper.setSubject("testing")
        mimeMessageHelper.setFrom("finalrestingheartrate@wired-heart.com")
        mimeMessageHelper.setText("mailContent", false)
        mimeMessageHelper.setTo("mwhlfrt@gmail.com")
        val message: MimeMessage = mimeMessageHelper.mimeMessage
        mailSender.send(message)
    }
}
