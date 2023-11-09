package com.example.chaincuerealestate.repositories

import com.example.chaincuerealestate.domains.Broker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrokerRepository : JpaRepository<Broker, String>

