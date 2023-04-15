package net.wohlfart.charon.service

import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import net.wohlfart.charon.controller.REQUEST_PARAM_TOKEN
import net.wohlfart.charon.controller.REQUEST_PATH_CONFIRM
import net.wohlfart.charon.dto.UserDto
import net.wohlfart.charon.mail.createRegistration
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class UserRegistrationService(
    val sendmailService: SendmailService,
    val oAuthProperties: OAuthProperties,
) {

    fun startRegistration(userDto: UserDto) {
        // generate & send the email
        sendmailService.sendEmail(
            createRegistration()
                .english()
                .put("username", userDto.username)
                .put("email", userDto.email)
                .put("tokenUrl", "${oAuthProperties.issuer}/$REQUEST_PATH_CONFIRM")
                .put("tokenKey", REQUEST_PARAM_TOKEN)
                .put("tokenValue", "testokenvaluehere")
        )
    }

    fun finishRegistration(tokenValue: String) {
        logger.info { "token value returned: $tokenValue" }
    }

}
