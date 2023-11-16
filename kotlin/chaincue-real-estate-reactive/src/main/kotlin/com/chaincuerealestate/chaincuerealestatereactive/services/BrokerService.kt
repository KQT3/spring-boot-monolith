package com.chaincuerealestate.chaincuerealestatereactive.services

import com.chaincuerealestate.chaincuerealestatereactive.domains.Broker
import com.chaincuerealestate.chaincuerealestatereactive.repositories.BrokerRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class BrokerService(private val brokerRepository: BrokerRepository) : BrokerServiceI {

    suspend fun save(email: String): Broker {
        val broker = Broker.create(email, "", "")
        return brokerRepository.save(broker)
    }

    suspend fun findAll(): Flow<Broker> {
        return brokerRepository.findAll()
    }
}
