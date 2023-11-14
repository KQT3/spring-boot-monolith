package com.chaincuerealestate.chaincuerealestatereactive.services

import com.chaincuerealestate.chaincuerealestatereactive.domains.Broker
import com.chaincuerealestate.chaincuerealestatereactive.repositories.BrokerRepository
import org.springframework.stereotype.Service

@Service
class BrokerService(private val brokerRepository: BrokerRepository) : BrokerServiceI {

    suspend fun save(email: String): Broker {
        val broker = Broker.create(email)
        return brokerRepository.save(broker)
    }
}
