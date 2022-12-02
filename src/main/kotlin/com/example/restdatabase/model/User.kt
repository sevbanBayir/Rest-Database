package com.example.restdatabase.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.*

/*@Entity
data class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0,
    val name: String,
    @Column(unique = true)
    val email: String,
) {
    @JsonIgnore
    var password: String = ""
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }
    fun comparePasswords(rawPassword: String): Boolean =
        BCryptPasswordEncoder().matches(rawPassword,password)
}*/
@Entity
data class User(
    var name: String = "",
    var email: String = "",
    var timestamp : Long = System.currentTimeMillis(),
    @OneToMany
    var boughtBooks : MutableList<Book>? = mutableListOf(),
    @OneToMany
    val favouriteBooks : MutableList<Book>? = mutableListOf(),
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
){
    @JsonIgnore
    var password = ""
        get() = field
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }
    fun comparePasswords(password : String) : Boolean {
            print(password)
            print(this.password)
            return BCryptPasswordEncoder().matches(password, this.password)
    }
}

