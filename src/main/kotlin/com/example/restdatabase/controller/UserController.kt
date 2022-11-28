package com.example.restdatabase.controller

import com.example.demo.dtos.RegisterDTO
import com.example.restdatabase.dto.Message
import com.example.restdatabase.dto.LoginDTO
import com.example.restdatabase.dto.PasswordDTO
import com.example.restdatabase.model.User
import com.example.restdatabase.service.UserService
import io.jsonwebtoken.Jwts
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import io.jsonwebtoken.SignatureAlgorithm;
import netscape.javascript.JSObject
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@RestController
@RequestMapping("api")
class UserController(private val userService: UserService) {

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @PostMapping("signUp")
    fun signUp(@RequestBody body: RegisterDTO): ResponseEntity<User> {

        val user = User()
        user.name = body.name
        user.email = body.email
        user.password = body.password
        return ResponseEntity.ok(userService.save(user))
    }

    @PostMapping("signIn")
    fun signIn(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {

        val user = userService.findByEmail(body.email)
            ?: return ResponseEntity.badRequest().body(Message("User not found"))

        if (!user.comparePasswords(body.password))
            return ResponseEntity.badRequest().body(Message("Invalid password"))

        val issuer = user.id.toString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000))
            .signWith(SignatureAlgorithm.HS512, "secret").compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true //very IMPORTANT

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("Succes"))
    }

    @PostMapping("forgotPassword")
    fun recoverPassword(@RequestBody body : LoginDTO) {
        val user = userService.findByEmail(body.email)
            ?: throw NoSuchElementException("There is not a user with given email")


        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        val newPassword = List(8) { charPool.random() }.joinToString { "" }
        BCryptPasswordEncoder().encode(newPassword)

        user.password = newPassword

        //newPasswordu email ile gönderme işlemi
    }

    @PostMapping("changePassword")
    fun changePassword(@RequestBody body: PasswordDTO, @CookieValue("jwt") jwt : String?) {
        if (jwt == null)
            throw NoSuchElementException("Not authenticated")

        val userBody = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
        val user = userService.getById(userBody.issuer.toInt())

        if (!user.comparePasswords(user.password))
            throw IllegalArgumentException("Current password is incorrect")

        if (body.newPassword == body.confirmPassword) {
            val encrypted = BCryptPasswordEncoder().encode(body.confirmPassword)
            user.password = encrypted
        }
    }

    @GetMapping("user/{id}")
    fun user(@PathVariable id : Int): User {

        if (allUser().any { it.id == id })
            throw NoSuchElementException("No user with the given ID")

        return userService.getById(id)
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {

        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok("success to delete")
    }

    @GetMapping("user")
    fun allUser(): Collection<User> {

        return userService.allUsers() ?: emptyList()
    }
}