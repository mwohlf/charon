package net.wohlfart.charon.service

import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import net.wohlfart.charon.controller.REQUEST_PARAM_TOKEN
import net.wohlfart.charon.controller.REQUEST_PATH_CONFIRM
import net.wohlfart.charon.dto.UserDto
import net.wohlfart.charon.entity.AuthUserDetails
import net.wohlfart.charon.entity.UserRegistration
import net.wohlfart.charon.mail.createRegistration
import net.wohlfart.charon.repository.RegistrationRepository
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class UserRegistrationService(
    val sendmailService: SendmailService,
    val registrationRepository: RegistrationRepository,
    val oAuthProperties: OAuthProperties,
) {

    fun startRegistration(userDto: UserDto) {
        // store the registration
        registrationRepository.save(
            UserRegistration(
                userDetails = AuthUserDetails(
                    username = userDto.username,
                    password = userDto.password,
                    email = userDto.email,
                )
            )
        )
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
