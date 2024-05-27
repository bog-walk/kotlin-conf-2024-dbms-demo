package dev.bogwalk.databases

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*

object DatabaseFactoryImpl : DatabaseFactory {
    private lateinit var driver: String
    private lateinit var url: String

    fun init(config: ApplicationConfig) {
        driver = config.property("storage.driverClassName").getString()
        url = config.property("storage.jdbcURL").getString()
    }

    override fun createHikariDataSource() = HikariDataSource(
        HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url
            maximumPoolSize = 3
            isAutoCommit = false
            validate()
        }
    )
}