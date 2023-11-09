package com.example.chaincuerealestate.services

import com.example.chaincuerealestate.domains.Broker
import com.example.chaincuerealestate.repositories.BrokerRepository
import org.springframework.stereotype.Service

@Service
class BrokerService(private val brokerRepository: BrokerRepository) : BrokerServiceI {

    fun save(email: String): Broker {
        val broker = Broker.create(email)
        return brokerRepository.save(broker)
    }
}
