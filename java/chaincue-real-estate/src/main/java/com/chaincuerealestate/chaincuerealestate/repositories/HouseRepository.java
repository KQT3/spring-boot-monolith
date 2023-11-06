package com.chaincuerealestate.chaincuerealestate.repositories;

import com.chaincuerealestate.chaincuerealestate.domains.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends JpaRepository<House, String> {
}
