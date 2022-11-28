package com.example.restdatabase.datasource

import com.example.restdatabase.model.Book

interface BankRepository {

    fun getBanks() : Collection<Book>
    fun getBank(accountNumber : String) : Book
    fun createBank(book : Book) : Book
    fun updateBank (book : Book) : Book
    fun deleteBank(accountNumber: String)
}