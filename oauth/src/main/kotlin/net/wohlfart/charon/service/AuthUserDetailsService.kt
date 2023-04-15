package net.wohlfart.charon.service

import net.wohlfart.charon.entity.AuthUserDetails
import net.wohlfart.charon.repository.AuthUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService(
    private val authUserRepository: AuthUserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {

    private final val delegate = InMemoryUserDetailsManager()

    init {
        /*
        @Suppress("DEPRECATION")
        delegate.createUser(User.withDefaultPasswordEncoder()
            .username("user")
            .password("pass")
            .roles("USER")
            .build())
         */
        authUserRepository.deleteAll()
        authUserRepository.save(
            AuthUserDetails(
                username = "user",
                // add "{bcrypt}"
                password = passwordEncoder.encode("pass"),
                email = "somewhere@host.com",
                enabled = true
            )
        )
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return authUserRepository.findByUsername(username)
        // return delegate.loadUserByUsername(username)
    }

    fun confirmRegistration(token: String) {

    }
}
