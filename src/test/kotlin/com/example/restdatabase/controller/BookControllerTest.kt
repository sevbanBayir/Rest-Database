package com.example.restdatabase.controller

import com.example.restdatabase.model.Book
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
internal class BookControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return all banks`() {
        //when/then
        mockMvc.get("/api/banks")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }
    }

    @Test
    fun `should return single bank with given accountNumber`() {
        //given
        val id = 8

        //when
        mockMvc.get("/api/banks/$id")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.trust") { value("2.0") }
            }
    }

    @Test
    fun `should return NOT FOUND if the given account number is not belong to any account`() {
        //given
        val id = 3

        //when
        mockMvc.get("/api/banks/$id")
            .andDo { print() }
            .andExpect { status { isNotFound() } }
    }

    @Test
    fun `should create new BANK if does not exist`() {
        //given
        val book = Book(id= 17,accountNumber = "fjhgjhyfgkhjuıjkll", trust =  2.0, transactionFee =  8)

        //when
        val performPost = mockMvc.post("/api/banks") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(book)
        }

        //then
        performPost
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(book))
                }
            }

//        mockMvc.get("/api/banks/${bank.id}")
//            .andExpect { content { json(objectMapper.writeValueAsString(bank)) } }
    }
}