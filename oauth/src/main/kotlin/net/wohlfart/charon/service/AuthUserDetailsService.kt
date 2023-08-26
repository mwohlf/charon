package net.wohlfart.charon.service

import net.wohlfart.charon.entity.AuthUserDetails
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

    fun createFederatedUser(email: String, federatedClient: String, federatedId: String): AuthUserDetails {
        // TODO: cleanup
        return authUserRepository.findByEmail(email) ?: authUserRepository.save(
            AuthUserDetails(
                email = email,
                federatedClient = federatedClient,
                federatedId = federatedId,
                enabled = true,
            )
        )
    }

    fun findByXid(xid: String): AuthUserDetails? {
        return authUserRepository.findByXid(xid)
    }

    fun findByEmail(email: String): AuthUserDetails? {
        return authUserRepository.findByEmail(email)
    }

    fun findByFederatedClientAndFederatedId(federatedClient: String, federatedId: String): AuthUserDetails? {
        return authUserRepository.findByFederatedClientAndFederatedId(federatedClient, federatedId)
    }
}

