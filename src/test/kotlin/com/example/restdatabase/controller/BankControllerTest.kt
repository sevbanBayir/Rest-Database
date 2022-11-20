package com.example.restdatabase.controller

import com.example.restdatabase.model.Bank
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity.status
import org.springframework.test.web.client.match.MockRestRequestMatchers.content
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.servlet.function.RequestPredicates.contentType

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest {

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
        val accountNumber = 1234

        //when
        mockMvc.get("/api/banks/$accountNumber")
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
        val accountNumber = "asdasd"

        //when
        mockMvc.get("/api/banks/$accountNumber")
            .andDo { print() }
            .andExpect { status { isNotFound() } }
    }

    @Test
    fun `should create new BANK if does not exist`() {
        //given
        val bank = Bank(4,"kkkk", 2.0, 8)

        //when
        val performPost = mockMvc.post("/api/banks") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(bank)
        }

        //then
        performPost
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(bank))
                }
            }

        mockMvc.get("/api/banks/${bank.accountNumber}")
            .andExpect { content { json(objectMapper.writeValueAsString(bank)) } }
    }
}