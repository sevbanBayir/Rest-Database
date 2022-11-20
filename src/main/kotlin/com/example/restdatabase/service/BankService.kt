package com.example.restdatabase.service

import com.example.restdatabase.datasource.CRUDRepository
import com.example.restdatabase.model.Bank
import org.springframework.stereotype.Service

@Service
class BankService (
    private val dataSource : CRUDRepository
) {

    fun getBanks() : Collection<Bank> = dataSource.findAll()
    fun getBank(id : Int) : Bank = dataSource.getReferenceById(id)
    fun createBank(bank : Bank) : Bank = dataSource.save(bank)
    fun updateBank (bank : Bank) : Bank = dataSource.save(bank)
    fun deleteBank(id : Int) = dataSource.deleteById(id)

}