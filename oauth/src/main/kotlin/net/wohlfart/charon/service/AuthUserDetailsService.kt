package net.wohlfart.charon.service

import net.wohlfart.charon.repository.AuthUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service



@Service
class AuthUserDetailsService(
    private val authUserRepository: AuthUserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails? {
        return authUserRepository.findByUsername(username)
    }

}
