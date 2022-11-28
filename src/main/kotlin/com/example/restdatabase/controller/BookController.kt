package com.example.restdatabase.controller

import com.example.restdatabase.model.Book
import com.example.restdatabase.service.BookService
import com.example.restdatabase.service.UserService
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.naming.AuthenticationException

@RestController
@RequestMapping("/api/books")
class BookController(private val service: BookService, private val userService: UserService) {

    private val books = service.getBooksByTimeStamp()

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuth(e : AuthenticationException) : ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @GetMapping()
    fun getBooks(@CookieValue jwt: String?): ResponseEntity<Any> {

        if (jwt == null)
            throw AuthenticationException("Not authenticated")

        return ResponseEntity.ok(service.getBooksByTimeStamp())
    }

    @GetMapping("/{categoryId}")
    fun getBooksByCategory(@PathVariable categoryId: Int,@CookieValue jwt: String?): Collection<Book> {

        if (jwt == null)
            throw AuthenticationException("Not authenticated")

        return service.getBooksByCategory(categoryId) ?: emptyList()
    }

    @PostMapping("buyBook/{bookId}")
    fun buyBook(@PathVariable bookId : Int, @CookieValue("jwt") jwt : String?) {
        if (jwt == null)
            throw AuthenticationException("Not authenticated")

        if (books.any { it.id == bookId })
            throw NoSuchElementException("No such book with given ID")

        val userBody = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
        val user = userService.getById(userBody.issuer.toInt())
        val bookToBuy = books.first { it.id == bookId }
        val stock = books.first { it.id == bookId }.stock

        val list = mutableSetOf(bookToBuy)
            user.boughtBooks = list
            books.first { it.id == bookId }.stock -= 1

    }

    @PostMapping("favBook/{bookId}")
    fun favBook(@PathVariable bookId : Int, @CookieValue("jwt") jwt : String?) {
        if (jwt == null)
            throw AuthenticationException("Not authenticated")

        val userBody = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
        val user = userService.getById(userBody.issuer.toInt())
        val bookToFav = books.first { it.id == bookId }
        user.favoriteBooks.add(bookToFav)
    }
}