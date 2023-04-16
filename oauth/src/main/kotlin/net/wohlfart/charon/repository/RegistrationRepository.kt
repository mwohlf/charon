package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.UserRegistration
import org.springframework.data.repository.CrudRepository


interface RegistrationRepository: CrudRepository<UserRegistration, Int> {

     fun findByTokenValue(tokenValue: String): UserRegistration

}


