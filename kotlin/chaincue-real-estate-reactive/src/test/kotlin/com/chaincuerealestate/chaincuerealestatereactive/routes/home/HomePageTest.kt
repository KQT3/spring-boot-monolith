package com.chaincuerealestate.chaincuerealestatereactive.routes.home

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.RepeatedTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class HomePageTest {

    @Autowired
    private lateinit var homePage: HomePage

    @RepeatedTest(100)
    fun `should return home page DTO`()  {
        runBlocking {
            homePage.homePage()
        }
    }
}
