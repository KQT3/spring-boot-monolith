package com.chaincuerealestate.chaincuerealestate.repositories;

import com.chaincuerealestate.chaincuerealestate.domains.Broker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, String> {
}
