package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.AuthUserDetails
import org.springframework.data.repository.CrudRepository


interface AuthUserRepository : CrudRepository<AuthUserDetails, Int> {

    fun findByUsername(username: String): AuthUserDetails?

    fun findByEmail(email: String): AuthUserDetails?

}
