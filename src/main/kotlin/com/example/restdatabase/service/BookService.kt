package com.example.restdatabase.service

import com.example.restdatabase.datasource.CRUDRepository
import com.example.restdatabase.model.Book
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class BookService (
    private val dataSource : CRUDRepository
) {

    fun getBooksByTimeStamp() : Collection<Book> = dataSource.findAll(Sort.by("timestamp").ascending())

    fun getBooksByCategory(categoryId : Int) : Collection<Book>? = dataSource.getBooksByCategoryID(categoryId)

    fun createBank(book : Book) : Book = dataSource.save(book)

    fun updateBank (book : Book) : Book = dataSource.save(book)

    fun deleteBank(id : Int) = dataSource.deleteById(id)

}