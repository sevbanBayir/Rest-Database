package com.example.restdatabase.service

import com.example.restdatabase.datasource.UserRepository
import com.example.restdatabase.model.User
import org.springframework.stereotype.Service

@Service
class UserService (private val userRepo : UserRepository) {

    fun save(user : User) : User = userRepo.save(user)

    fun findByEmail(email : String) : User? = userRepo.findByEmail(email)

    fun getById(id : Int) : User = userRepo.getById(id)
}