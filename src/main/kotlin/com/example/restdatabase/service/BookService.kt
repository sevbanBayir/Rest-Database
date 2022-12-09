package com.example.restdatabase.service

import com.example.restdatabase.datasource.BookRepository
import com.example.restdatabase.model.Book
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class BookService (
    private val dataSource : BookRepository
) {
    fun getBooksByTimeStamp() : Collection<Book> = dataSource.findAll(Sort.by("timestamp").ascending())

    fun getBooksByCategory(categoryId : Int) : Collection<Book>? = dataSource.getBooksByCategoryID(categoryId)

    fun createBook(book : Book) : Book = dataSource.save(book)

    fun updateBook (book : Book) : Book = dataSource.save(book)

    fun deleteBook(id : Int) = dataSource.deleteById(id)
}