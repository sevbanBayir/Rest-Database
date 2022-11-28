package com.example.restdatabase.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

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
class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0

    @Column
    var name = ""

    @Column(unique = true)
    var email = ""

    @Column
    var timestamp : Long = System.currentTimeMillis()

    @OneToMany(cascade = [CascadeType.ALL])
    var favoriteBooks : MutableSet<Book> = mutableSetOf()

    @OneToMany(cascade = [CascadeType.ALL])
    var boughtBooks : MutableSet<Book> = mutableSetOf()

    @Column
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

