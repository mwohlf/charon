package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.AuthUserDetails
import net.wohlfart.charon.entity.ExternalToken
import org.springframework.data.repository.CrudRepository


interface ExternalTokenRepository: CrudRepository<ExternalToken, Int> {

    fun findAllByAuthUserDetails(authUserDetails: AuthUserDetails): Set<ExternalToken>

}
