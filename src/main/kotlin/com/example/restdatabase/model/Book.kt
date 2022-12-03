package com.example.restdatabase.model

import java.time.Clock
import java.time.LocalDate
import javax.persistence.*

@Entity
data class Book(
    var categoryID : Int = 0,
    var name : String = "",
    var author : String = "",
    var pageNumber : Int = -1,
    var stock : Int = 0,
    var timestamp: LocalDate? = LocalDate.now(Clock.systemDefaultZone()),
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = 0
)
