package com.example.restdatabase.service

import com.example.restdatabase.datasource.BookRepository
import com.example.restdatabase.datasource.OrderRepository
import com.example.restdatabase.model.Book
import com.example.restdatabase.model.Order
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class OrderService (
    private val dataSource : OrderRepository
) {

    fun createOrder(order : Order) : Order = dataSource.save(order)

    fun updateOrder(order : Order) : Order = dataSource.save(order)

    fun deleteOrder(id : Int) = dataSource.deleteById(id)
}