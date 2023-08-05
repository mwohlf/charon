package net.wohlfart.charon.service

import mu.KotlinLogging
import net.wohlfart.charon.entity.AuthUserDetails
import net.wohlfart.charon.entity.ExternalToken
import net.wohlfart.charon.entity.TokenProvider
import net.wohlfart.charon.entity.TokenType
import net.wohlfart.charon.repository.AuthUserRepository
import net.wohlfart.charon.repository.ExternalTokenRepository
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class TokenService(
    private val authUserRepository: AuthUserRepository,
    private val externalTokenRepository: ExternalTokenRepository,
) {

    fun archive(
        idToken: OidcIdToken,
        accessToken: OAuth2AccessToken,
        refreshToken: OAuth2RefreshToken?
    ) {
        logger.info { "idToken: $idToken" }
        logger.info { "accessToken: $accessToken" }
        logger.info { "refreshToken: $refreshToken" }

        val authUserDetails = getOrCreateUserDetails(idToken.email)

        externalTokenRepository.save(
            ExternalToken(
                authUserDetails = authUserDetails,
                type = TokenType.ID,
                provider = TokenProvider.GOOGLE,
                value = idToken.tokenValue
            )
        )

        externalTokenRepository.save(
            ExternalToken(
                authUserDetails = authUserDetails,
                type = TokenType.ACCESS,
                provider = TokenProvider.GOOGLE,
                value = accessToken.tokenValue
            )
        )

        if (refreshToken != null) {
            externalTokenRepository.save(
                ExternalToken(
                    authUserDetails = authUserDetails,
                    type = TokenType.REFRESH,
                    provider = TokenProvider.GOOGLE,
                    value = refreshToken.tokenValue
                )
            )
        }
    }

    private fun getOrCreateUserDetails(email: String): AuthUserDetails {
        return authUserRepository.findByEmail(email) ?: authUserRepository.save(
            AuthUserDetails(
                username = email,
                email = email,
                enabled = true,
            )
        )
    }

}
