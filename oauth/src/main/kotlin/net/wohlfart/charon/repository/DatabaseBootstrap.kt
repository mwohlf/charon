package net.wohlfart.charon.repository

import net.wohlfart.charon.entity.AuthUserDetails
import net.wohlfart.charon.entity.Authority
import net.wohlfart.charon.entity.AuthorityIdentifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import org.springframework.security.crypto.password.PasswordEncoder
import javax.sql.DataSource


@Configuration
class DatabaseBootstrap(
    authUserRepository: AuthUserRepository,
    authorityRepository: AuthorityRepository,
    private val passwordEncoder: PasswordEncoder,
    registrationRepository: RegistrationRepository,
) {

    init {
        // remove first because they might be referenced from users
        registrationRepository.deleteAll()
        // then the users and permissions
        authUserRepository.deleteAll()
        authorityRepository.deleteAll()
        // then re-create
        authorityRepository.saveAll(
            AuthorityIdentifier.entries.map { authorityIdentifier: AuthorityIdentifier -> Authority(authorityIdentifier) })
        val adminRole = authorityRepository.findByIdentifier(AuthorityIdentifier.ROLE_ADMIN)
        val userRole = authorityRepository.findByIdentifier(AuthorityIdentifier.ROLE_USER)
        val generalRole = authorityRepository.findByIdentifier(AuthorityIdentifier.ROLE_GENERAL)
        authUserRepository.save(bootstrapUser(arrayOf(adminRole, userRole, generalRole)))
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
        runSqlScript(
            dataSource,
            "sql/drop-tables.sql"
        )
        runSqlScript(
            dataSource,
            //"org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql"
            "sql/oauth2-authorization-schema.sql"
        )
        runSqlScript(
            dataSource,
            //"org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql"
            "sql/oauth2-authorization-consent-schema.sql"
        )
        runSqlScript(
            dataSource,
            //"org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql"
            "sql/oauth2-registered-client-schema.sql"
        )
        return JdbcTemplate(dataSource)
    }

    private fun runSqlScript(dataSource: DataSource, scriptPath: String) {
        val resource = ClassPathResource(scriptPath)
        val databasePopulator = ResourceDatabasePopulator(resource)
        databasePopulator.execute(dataSource)
    }

    private final fun bootstrapUser(authorities: Array<Authority>): AuthUserDetails {
        val result = AuthUserDetails(
            username = "user",
            // checkout https://thorben-janssen.com/hibernate-enum-mappings/
            // for a converter
            password = passwordEncoder.encode("pass"),
            email = "somewhere@host.com",
            enabled = true
        )
        authorities.asIterable().forEach {
            result.authorities.add(it)
        }
        return result
    }
}
