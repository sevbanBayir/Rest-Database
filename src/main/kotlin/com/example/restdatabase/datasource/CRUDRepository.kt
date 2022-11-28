package com.example.restdatabase.datasource

import com.example.restdatabase.model.Book
import org.hibernate.mapping.Collection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Collections

interface CRUDRepository : JpaRepository<Book, Int> {

    @Query("SELECT * FROM book WHERE categoryID= :categoryId ", nativeQuery = true)
    fun getBooksByCategoryID (categoryId : Int) : kotlin.collections.Collection<Book>

}