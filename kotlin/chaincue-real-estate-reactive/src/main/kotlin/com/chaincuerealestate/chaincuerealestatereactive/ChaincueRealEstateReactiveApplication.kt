package com.chaincuerealestate.chaincuerealestatereactive

import com.chaincuerealestate.chaincuerealestatereactive.domains.Broker
import com.chaincuerealestate.chaincuerealestatereactive.repositories.BrokerRepository
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ChaincueRealEstateReactiveApplication {

    @Bean
    fun init(
        brokerRepository: BrokerRepository
    ) = ApplicationRunner {

        runBlocking {
            val broker1 = Broker.create("")
            brokerRepository.save(broker1)
        }

    }
}

fun main(args: Array<String>) {
    runApplication<ChaincueRealEstateReactiveApplication>(*args)
}
