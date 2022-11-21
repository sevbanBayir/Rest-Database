package com.example.restdatabase.controller

import com.example.demo.dtos.RegisterDto
import com.example.restdatabase.dto.Message
import com.example.restdatabase.dto.LoginDTO
import com.example.restdatabase.model.User
import com.example.restdatabase.service.UserService
import io.jsonwebtoken.Jwts
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("api")
class UserController(
    private val userService: UserService
) {
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(e: NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(e: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @PostMapping("register")
    fun register(@RequestBody body: RegisterDto): ResponseEntity<User> {
/*        val user = User(name = body.name, email = body.email)
        user.password = body.password

        return ResponseEntity.ok(userService.save(user))*/

        val user = User()
        user.name = body.name
        user.email = body.email
        user.password = body.password
        return ResponseEntity.ok(this.userService.save(user))
    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
/*        val user = userService.findByEmail(body.email)
            ?: throw IllegalArgumentException("Wrong Email")

        if (!user.comparePasswords(body.password))
            throw IllegalArgumentException("Wrong Password")

        val issuer = user.id.toString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000))
            .signWith(SignatureAlgorithm.HS512, "secret").compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true //very IMPORTANT

        response.addCookie(cookie)

        return ResponseEntity.ok("Succes")*/

        val user = this.userService.findByEmail(body.email)
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

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {
        try {

            if (jwt == null)
                return ResponseEntity.status(401).body("Not authenticated")
            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
            return ResponseEntity.ok(this.userService.getById(body.issuer.toInt()))

        } catch (e: Exception) {
            return ResponseEntity.status(401).body("Not authenticated")
        }
    }

    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {

        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok("success to delete")
    }
}