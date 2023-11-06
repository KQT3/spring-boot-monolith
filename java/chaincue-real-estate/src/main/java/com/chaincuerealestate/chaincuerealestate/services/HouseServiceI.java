package com.chaincuerealestate.chaincuerealestate.services;

import com.chaincuerealestate.chaincuerealestate.domains.House;

import java.util.List;

public interface HouseServiceI {
    House save(House.HouseTypes houseTypes);

    House findById(String id);

    List<House> findAll();
}
