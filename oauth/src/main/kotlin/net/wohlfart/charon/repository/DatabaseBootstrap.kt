package net.wohlfart.charon.repository

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import javax.sql.DataSource

@Configuration
class DatabaseBootstrap {

    @Bean
    fun embeddedDatabase(dataSource: DataSource): JdbcTemplate {
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

}
