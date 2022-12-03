package com.example.restdatabase.controller

import com.example.restdatabase.model.Book
import com.example.restdatabase.model.User
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
    fun handleAuth(e: AuthenticationException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.UNAUTHORIZED)

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    fun checkAuthentication(jwt: String?) : User {
        if (jwt == null)
            throw AuthenticationException("Unauthorized")

        val userId = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body.issuer.toInt()

        return userService.getById(userId)
    }

    @GetMapping()
    fun getBooks(@CookieValue jwt: String?): Collection<Book> {

        checkAuthentication(jwt)

        return service.getBooksByTimeStamp()
    }

    @GetMapping("/{categoryId}")
    fun getBooksByCategory(@PathVariable categoryId: Int, @CookieValue jwt: String?): Collection<Book> {

        checkAuthentication(jwt)

        return service.getBooksByCategory(categoryId) ?: emptyList()
    }

    @PostMapping("createBook")
    fun createBook(@RequestBody body: Book,@CookieValue jwt: String?) {
        checkAuthentication(jwt)
        val book = Book(
            name = body.name,
            stock = body.stock,
            author = body.author,
            categoryID = body.categoryID,
            pageNumber = body.pageNumber
        )

        service.createBook(book)
    }

    @PatchMapping("buyBook/{bookId}")
    fun buyBook(@PathVariable bookId: Int, @CookieValue("jwt") jwt: String?) {

        checkAuthentication(jwt)

        if (!books.any { it.id == bookId })
            throw NoSuchElementException("No such book with given ID")

        val userBody = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
        val user = userService.getById(userBody.issuer.toInt())
        val bookToBuy = books.first { it.id == bookId }

        if (bookToBuy.stock > 0) {
            user.boughtBooks?.add(bookToBuy)
            bookToBuy.stock -= 1
        } else throw IllegalArgumentException("The book with given id is out of stock :(")

        service.updateBook(bookToBuy)
        userService.save(user)
    }


    @PatchMapping("favBook/{bookId}")
    fun favBook(@PathVariable bookId: Int, @CookieValue("jwt") jwt: String?) {

        checkAuthentication(jwt)

        val userBody = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
        val user = userService.getById(userBody.issuer.toInt())
        val bookToFav = books.first { it.id == bookId }

        user.favouriteBooks?.add(bookToFav)

        userService.save(user)
    }
}
