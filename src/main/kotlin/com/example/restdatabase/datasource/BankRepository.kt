package com.example.restdatabase.datasource

import com.example.restdatabase.model.Bank

interface BankRepository {

    fun getBanks() : Collection<Bank>
    fun getBank(accountNumber : String) : Bank
    fun createBank(bank : Bank) : Bank
    fun updateBank (bank : Bank) : Bank
    fun deleteBank(accountNumber: String)
}