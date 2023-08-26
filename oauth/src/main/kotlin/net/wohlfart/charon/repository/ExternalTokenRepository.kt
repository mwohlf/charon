package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.AuthUserDetails
import net.wohlfart.charon.entity.ExternalToken
import net.wohlfart.charon.entity.ExternalTokenId
import net.wohlfart.charon.entity.TokenType
import org.springframework.data.repository.CrudRepository


interface ExternalTokenRepository : CrudRepository<ExternalToken, ExternalTokenId> {

    fun findAllByAuthUserDetails(authUserDetails: AuthUserDetails): Set<ExternalToken>

    fun findAllByTypeAndAuthUserDetails(access: TokenType, authUserDetails: AuthUserDetails): Set<ExternalToken>

}
