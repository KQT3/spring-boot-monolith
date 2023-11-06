package com.chaincuerealestate.chaincuerealestate.services;

import com.chaincuerealestate.chaincuerealestate.domains.Broker;
import com.chaincuerealestate.chaincuerealestate.repositories.BrokerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BrokerService implements BrokerServiceI {
    private final BrokerRepository brokerRepository;

    public Broker save(String email) {
        Broker broker = Broker.create(email);
        return brokerRepository.save(broker);
    }

}
