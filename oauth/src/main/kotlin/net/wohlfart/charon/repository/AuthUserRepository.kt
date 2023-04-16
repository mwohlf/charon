package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.AuthUserDetails
import org.springframework.data.repository.CrudRepository


interface AuthUserRepository: CrudRepository<AuthUserDetails, Long> {

    fun findByUsername(username: String): AuthUserDetails

}
