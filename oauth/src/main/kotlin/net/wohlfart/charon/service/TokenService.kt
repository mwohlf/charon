package net.wohlfart.charon.service

import mu.KotlinLogging
import net.wohlfart.charon.entity.AuthUserDetails
import net.wohlfart.charon.entity.ExternalToken
import net.wohlfart.charon.entity.TokenType
import net.wohlfart.charon.repository.ExternalTokenRepository
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class TokenService(
    private val externalTokenRepository: ExternalTokenRepository,
) {

    fun archive(
        authUserDetails: AuthUserDetails,
        idToken: OidcIdToken,
        accessToken: OAuth2AccessToken,
        refreshToken: OAuth2RefreshToken?
    ) {
        logger.info { "idToken: $idToken" }
        logger.info { "accessToken: $accessToken" }
        logger.info { "refreshToken: $refreshToken" }


        externalTokenRepository.save(
            ExternalToken(
                authUserDetails = authUserDetails,
                type = TokenType.ID,
                value = idToken.tokenValue
            )
        )

        externalTokenRepository.save(
            ExternalToken(
                authUserDetails = authUserDetails,
                type = TokenType.ACCESS,
                value = accessToken.tokenValue
            )
        )

        if (refreshToken != null) {
            externalTokenRepository.save(
                ExternalToken(
                    authUserDetails = authUserDetails,
                    type = TokenType.REFRESH,
                    value = refreshToken.tokenValue
                )
            )
        }
    }



}
