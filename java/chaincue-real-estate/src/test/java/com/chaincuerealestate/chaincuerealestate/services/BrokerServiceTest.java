package com.chaincuerealestate.chaincuerealestate.services;

import com.chaincuerealestate.chaincuerealestate.domains.Broker;
import com.chaincuerealestate.chaincuerealestate.repositories.BrokerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BrokerServiceTest {
    @InjectMocks
    BrokerService brokerService;
    @Mock
    BrokerRepository brokerRepository;

    @Test
    void save() {
        // Given
        String email = "test@example.com";
        Broker broker = Broker.create(email);
        Mockito.when(brokerRepository.save(Mockito.any(Broker.class))).thenReturn(broker);

        // When
        Broker save = brokerService.save(email);

        // Then
        Mockito.verify(brokerRepository, Mockito.times(1)).save(Mockito.any(Broker.class));
    }
}
