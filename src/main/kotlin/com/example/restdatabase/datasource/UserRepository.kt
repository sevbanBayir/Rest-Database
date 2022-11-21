package com.example.restdatabase.datasource

import com.example.restdatabase.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Int> {
    fun findByEmail (email : String) : User?
}