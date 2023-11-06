package com.chaincuerealestate.chaincuerealestate.routes.home;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class HomePageTest {
    @Autowired
    HomePage homePage;

    @BeforeEach
    void setUp() {
    }

    @Test
    void homePage() {
        var dtoResponseEntity = homePage.homePage("token").getBody();

        assertNotNull(dtoResponseEntity);
        assertTrue(dtoResponseEntity.countries().length > 0);
        assertTrue(dtoResponseEntity.recentlyAddedHouses().length > 0);
    }
}
