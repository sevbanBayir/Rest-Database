package com.example.restdatabase.model

import javax.persistence.*

@Entity
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0,
    val categoryID : Int = 0,
    val name : String = "",
    val author : String = "",
    val pageNumber : Int = -1,
    var stock : Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
