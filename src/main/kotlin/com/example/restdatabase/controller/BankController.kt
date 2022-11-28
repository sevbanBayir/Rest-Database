package com.example.restdatabase.controller

import com.example.restdatabase.model.Bank
import com.example.restdatabase.service.BankService
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/banks")
class BankController(private val service: BankService) {

    private val books = service.getBanks()

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping
    fun getBanks(@CookieValue jwt: String?): ResponseEntity<Any> {

        if (jwt == null)
            return ResponseEntity.status(401).body("Not authenticated")
        return ResponseEntity.ok(service.getBanks())
    }

    @GetMapping("/{id}")
    fun getBank(@PathVariable id: Int): Bank {
        if (!banks.any { it.id == id })
            throw NoSuchElementException("Any element with given id ($id) was not found.")

        return service.getBank(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createBank(@RequestBody bank: Bank): Bank {

        if (banks.any { it.accountNumber == bank.accountNumber })
            throw IllegalArgumentException("The element with given account number (${bank.accountNumber}) already exists.")

        val newBank = Bank(
            accountNumber = bank.accountNumber,
            trust = bank.trust,
            transactionFee = bank.transactionFee
        )
        service.createBank(newBank)

        return newBank
    }

    @PatchMapping
    fun updateBank(@RequestBody bank: Bank) {

        val currentBank = banks.firstOrNull { it.accountNumber == bank.accountNumber }
            ?: throw IllegalArgumentException("There is no element with account number : ${bank.accountNumber}")

        service.deleteBank(currentBank.id)
        service.updateBank(bank)
    }

    @DeleteMapping("/{id}")
    fun deleteBank(@PathVariable id: Int) {
        banks.firstOrNull { it.id == id }
            ?: throw NoSuchElementException("There is no element with given id ($id) to delete")
        service.deleteBank(id)
    }
}