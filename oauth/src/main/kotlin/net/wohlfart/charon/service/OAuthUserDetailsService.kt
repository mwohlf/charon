package net.wohlfart.charon.service

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.stereotype.Service

@Service
class OAuthUserDetailsService: UserDetailsService {

    private final val delegate = InMemoryUserDetailsManager()

    init {
        delegate.createUser(User.withDefaultPasswordEncoder()
            .username("user")
            .password("pass")
            .roles("USER")
            .build())
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return delegate.loadUserByUsername(username)
    }

}
