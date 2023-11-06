package com.chaincuerealestate.chaincuerealestate.services;

import com.chaincuerealestate.chaincuerealestate.domains.HouseImage;

import java.util.List;

public interface HouseImageServiceI {
    HouseImage save(String url);

    HouseImage findById(String id);

    List<HouseImage> findAll();
}
