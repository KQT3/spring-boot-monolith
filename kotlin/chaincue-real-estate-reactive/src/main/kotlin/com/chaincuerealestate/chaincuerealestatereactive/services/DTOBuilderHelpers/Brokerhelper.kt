package com.chaincuerealestate.chaincuerealestatereactive.services.DTOBuilderHelpers

import com.chaincuerealestate.chaincuerealestatereactive.domains.Broker
import com.chaincuerealestate.chaincuerealestatereactive.domains.House
import com.chaincuerealestate.chaincuerealestatereactive.services.BrokerService
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component

@Component
class Brokerhelper(private val brokerService: BrokerService) {

    fun <B> updateDTOBuilderWithBrokers(setHouses: suspend (B, List<Broker>) -> Unit): suspend(B) -> B {
        return { dtoBuilder ->
            val houses = brokerService.findAll().toList()
            setHouses(dtoBuilder, houses)
            dtoBuilder
        }
    }

}

