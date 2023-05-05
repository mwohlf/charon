package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.Authority
import net.wohlfart.charon.entity.AuthorityIdentifier
import org.springframework.data.repository.CrudRepository


interface AuthorityRepository: CrudRepository<Authority, Int> {

    fun findByIdentifier(authorityIdentifier: AuthorityIdentifier): Authority

}
