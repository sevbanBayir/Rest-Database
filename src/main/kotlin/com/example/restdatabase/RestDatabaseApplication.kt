package com.example.restdatabase

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [
//    DataSourceAutoConfiguration::class,
//    DataSourceTransactionManagerAutoConfiguration::class,
//    HibernateJpaAutoConfiguration::class,
    SecurityAutoConfiguration::class
])
class RestDatabaseApplication

fun main(args: Array<String>) {
    runApplication<RestDatabaseApplication>(*args)
}
