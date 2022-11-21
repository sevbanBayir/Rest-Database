package com.example.restdatabase

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
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
