package com.example.restdatabase.datasource

import com.example.restdatabase.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookRepository : JpaRepository<Book, Int> {

    @Query("SELECT * FROM book WHERE categoryID= :categoryId ", nativeQuery = true)
    fun getBooksByCategoryID (categoryId : Int) : Collection<Book>

}