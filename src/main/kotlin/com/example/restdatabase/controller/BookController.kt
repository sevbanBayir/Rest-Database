package com.example.restdatabase.controller

import com.example.restdatabase.model.Book
import com.example.restdatabase.model.Order
import com.example.restdatabase.model.User
import com.example.restdatabase.service.BookService
import com.example.restdatabase.service.OrderService
import com.example.restdatabase.service.UserService
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.naming.AuthenticationException

@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val service: BookService,
    private val userService: UserService,
    private val orderService: OrderService
) {

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

    fun checkAuthentication(jwt: String?): User {
        if (jwt == null)
            throw AuthenticationException("Unauthorized")

        val userId = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body.issuer.toInt()

        return userService.getById(userId)
    }

    fun fetchBook(bookId: Int): Book {
        val book = books.firstOrNull { it.id == bookId }
            ?: throw NoSuchElementException("Can not found book with id : $bookId")
        return book
    }

    @GetMapping()
    fun getBooks(@CookieValue("jwt") jwt: String?): Collection<Book> {

        checkAuthentication(jwt)
        return service.getBooksByTimeStamp()
    }

    @GetMapping("/{categoryId}")
    fun getBooksByCategory(@PathVariable categoryId: Int, @CookieValue("jwt") jwt: String?): Collection<Book> {
        checkAuthentication(jwt)
        return service.getBooksByCategory(categoryId) ?: emptyList()
    }

    @PostMapping("createBook")
    fun createBook(@RequestBody body: Book, @CookieValue("jwt") jwt: String?) {
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

    @PatchMapping("buyBook/")
    fun buyBook(@CookieValue("jwt") jwt: String?) {

        val user = checkAuthentication(jwt)
        //val bookToBuy = fetchBook(bookId)
        val newOrder = Order(user = user)
        val newInCartList = mutableListOf<Book>()

        newInCartList.addAll(user.inCart)
        newInCartList.forEach { bookToBuy -> //Direkt user.InCart listesine iteration uygularsam hata veriyor.

            if (bookToBuy.stock > 0) {
                bookToBuy.stock -= 1
                bookToBuy.isBought = true
                user.boughtBooks.add(bookToBuy)
            }
            service.updateBook(bookToBuy)
        }
        user.inCart = newInCartList
        userService.save(user)
        newOrder.items = user.inCart
        orderService.createOrder(newOrder)
    }

        @PatchMapping("addToCart/{bookId}")
        fun addToCart(@PathVariable bookId: Int, @CookieValue("jwt") jwt: String?) {
            val user = checkAuthentication(jwt)
            val book = fetchBook(bookId)

            if (book.stock > 0) {
                book.isInCart = true
                user.inCart.add(book)
                userService.save(user)
            } else throw IllegalArgumentException("The book with given id is out of stock :(")
        }

        @PatchMapping("favBook/{bookId}")
        fun favBook(@PathVariable bookId: Int, @CookieValue("jwt") jwt: String?) {

            val user = checkAuthentication(jwt)
            val bookToFav = fetchBook(bookId)

            user.favouriteBooks.add(bookToFav)
            userService.save(user)
        }
    }

