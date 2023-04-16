package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.UserRegistration
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component


@Component
interface RegistrationRepository: CrudRepository<UserRegistration, Long> {

}


