package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.UserRegistration
import org.springframework.data.repository.CrudRepository


interface RegistrationRepository: CrudRepository<UserRegistration, Long> {

     fun findByTokenValue(tokenValue: String): UserRegistration

}


