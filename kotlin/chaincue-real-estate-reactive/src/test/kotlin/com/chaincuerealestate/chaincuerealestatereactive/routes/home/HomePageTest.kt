package com.chaincuerealestate.chaincuerealestatereactive.routes.home

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.Repeat
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
class HomePageTest {

    @Autowired
    private lateinit var homePage: HomePage

    @RepeatedTest(100)
    fun `should return home page DTO`() {
        runBlocking {
            homePage.homePage()
        }
    }
}
