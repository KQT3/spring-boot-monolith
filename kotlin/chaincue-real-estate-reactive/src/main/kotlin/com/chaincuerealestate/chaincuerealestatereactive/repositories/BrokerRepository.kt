package com.chaincuerealestate.chaincuerealestatereactive.repositories

import com.chaincuerealestate.chaincuerealestatereactive.domains.Broker
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BrokerRepository : CoroutineCrudRepository<Broker, String>

