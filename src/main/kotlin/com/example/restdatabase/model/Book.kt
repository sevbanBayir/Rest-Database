package com.example.restdatabase.model

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Book(
    var isBought : Boolean = false,
    var isInCart : Boolean = false,
    var categoryID : Int = 0,
    var name : String = "",
    var author : String = "",
    var pageNumber : Int = -1,
    var stock : Int = 0,
    var timestamp: LocalDate? = LocalDateTime.now().toLocalDate(),
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = 0
)
