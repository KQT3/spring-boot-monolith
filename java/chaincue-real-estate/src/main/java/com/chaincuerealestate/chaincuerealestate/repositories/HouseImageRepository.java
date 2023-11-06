package com.chaincuerealestate.chaincuerealestate.repositories;

import com.chaincuerealestate.chaincuerealestate.domains.HouseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseImageRepository extends JpaRepository<HouseImage, String> {
}
