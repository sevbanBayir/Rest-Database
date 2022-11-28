package com.example.restdatabase.datasource

/*
@Repository

class BankRepositoryImp : BankRepository {


     val banks = mutableListOf(
        Bank(1,"1234", 2.0, 83),
        Bank(2,"abcd", 5.0, 34),
        Bank(3,"efgh", 3.0, 54),
    )

    override fun getBanks(): Collection<Bank> = banks

    override fun getBank(accountNumber: String): Bank =
        banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")

    override fun createBank(bank: Bank): Bank {
        if (banks.any { it.accountNumber == bank.accountNumber })
            throw IllegalArgumentException("Bank with the given account number ${bank.accountNumber} already exists.")
        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun deleteBank(accountNumber: String) {
        TODO("Not yet implemented")
    }

}*/
