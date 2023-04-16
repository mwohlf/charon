package net.wohlfart.charon.service

import net.wohlfart.charon.entity.AuthUserDetails
import net.wohlfart.charon.repository.AuthUserRepository
import net.wohlfart.charon.repository.RegistrationRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService(
    private val authUserRepository: AuthUserRepository,
    registrationRepository: RegistrationRepository,
    passwordEncoder: PasswordEncoder,
) : UserDetailsService {

    init {
        // remove first because they might be referenced from users
        registrationRepository.deleteAll()
        // then the users
        authUserRepository.deleteAll()
        authUserRepository.save(
            AuthUserDetails(
                username = "user",
                password = passwordEncoder.encode("pass"),
                email = "somewhere@host.com",
                enabled = true
            )
        )
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return authUserRepository.findByUsername(username)
    }

}
