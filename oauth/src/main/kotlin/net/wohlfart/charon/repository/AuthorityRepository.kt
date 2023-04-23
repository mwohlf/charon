package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.Authority
import net.wohlfart.charon.entity.AuthorityName
import org.springframework.data.repository.CrudRepository


interface AuthorityRepository: CrudRepository<Authority, Int> {

    fun findByName(authorityName: AuthorityName): Authority

}
