package com.example.restdatabase.model

import javax.persistence.*

@Entity
data class Bank(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = 0,
    @Column(unique = true)
    val accountNumber : String,
    val trust : Double,
    val transactionFee : Int
)
