package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.AuthUserDetails
import org.springframework.data.repository.CrudRepository


interface AuthUserRepository : CrudRepository<AuthUserDetails, Int> {

    fun findByFederatedClientAndFederatedId(federatedClient: String, federatedId: String): AuthUserDetails?

    fun findByUsername(username: String): AuthUserDetails?

    fun findByEmail(email: String): AuthUserDetails?

    fun findByXid(xid: String): AuthUserDetails?

}
