package net.wohlfart.charon.service

import jakarta.servlet.http.HttpSession
import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import net.wohlfart.charon.controller.REQUEST_PARAM_TOKEN
import net.wohlfart.charon.controller.REQUEST_PATH_CONFIRM
import net.wohlfart.charon.dto.UserDto
import net.wohlfart.charon.entity.AuthUserDetails
import net.wohlfart.charon.entity.UserRegistration
import net.wohlfart.charon.exception.TokenNotFoundException
import net.wohlfart.charon.mail.createRegistration
import net.wohlfart.charon.repository.AuthUserRepository
import net.wohlfart.charon.repository.RegistrationRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.authentication.RememberMeAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


private val logger = KotlinLogging.logger {}

@Service
class UserRegistrationService(
    val sendmailService: SendmailService,
    val registrationRepository: RegistrationRepository,
    val authUserRepository: AuthUserRepository,
    val oAuthProperties: OAuthProperties,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun startRegistration(userDto: UserDto) {
        // store the registration
        val registration = registrationRepository.save(
            UserRegistration(
                userDetails = AuthUserDetails(
                    username = userDto.username,
                    password = passwordEncoder.encode(userDto.password),
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
                .put("tokenUrl", "${oAuthProperties.issuer}$REQUEST_PATH_CONFIRM")
                .put("tokenKey", REQUEST_PARAM_TOKEN)
                .put("tokenValue", registration.tokenValue)
        )
    }

    @Transactional
    fun finishRegistration(session: HttpSession, tokenValue: String) {
        try {
            val registration = registrationRepository.findByTokenValue(tokenValue)
            var userDetails = registration.userDetails!!
            userDetails.enabled = true
            authUserRepository.save(userDetails)
            session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContextWithAuthentication(tokenValue, userDetails)
            )
        } catch (ex: EmptyResultDataAccessException) {
            throw TokenNotFoundException(ex)
        }
    }

    fun securityContextWithAuthentication(tokenValue: String, authUserDetails: AuthUserDetails): SecurityContext {
        // RememberMeAuthenticationToken, UsernamePasswordAuthenticationToken
        val securityContext = SecurityContextHolder.getContext()
        securityContext.authentication = RememberMeAuthenticationToken(tokenValue, authUserDetails, authUserDetails.grantedAuthorities)
        return securityContext
    }

}
