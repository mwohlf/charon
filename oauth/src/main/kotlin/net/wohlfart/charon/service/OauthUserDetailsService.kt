package net.wohlfart.charon.service

import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.stereotype.Service

@Service
class OauthUserDetailsService: UserDetailsService {

    private final val delegate = InMemoryUserDetailsManager()

    init {
        delegate.createUser(User.withDefaultPasswordEncoder()
            .username("user1")
            .password("s3cr37")
            .roles("USER")
            .build())
    }

    override fun loadUserByUsername(username: String): UserDetails {
        return delegate.loadUserByUsername(username)
    }

}
