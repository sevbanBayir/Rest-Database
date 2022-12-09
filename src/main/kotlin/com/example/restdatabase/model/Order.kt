package com.example.restdatabase.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "orders")
data class Order(
    @OneToMany
    var items : MutableList<Book> = mutableListOf(),
    @OneToOne
    val user : User,
    val time : LocalDateTime = LocalDateTime.now(),
    val orderStatus : OrderStatus = OrderStatus.PENDING,
    val quantity : Int,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = -1
)
