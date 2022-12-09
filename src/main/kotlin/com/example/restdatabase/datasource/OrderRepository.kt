package com.example.restdatabase.datasource

import com.example.restdatabase.model.Book
import com.example.restdatabase.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderRepository : JpaRepository<Order, Int>