package com.chaincuerealestate.chaincuerealestate.services;

import com.chaincuerealestate.chaincuerealestate.domains.House;
import com.chaincuerealestate.chaincuerealestate.exceptions.HouseNotFoundException;
import com.chaincuerealestate.chaincuerealestate.repositories.HouseRepository;
import com.chaincuerealestate.chaincuerealestate.utilities.AweS3Urls;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseService implements HouseServiceI {
    private final HouseRepository houseRepository;

    @Override
    public House save(House.HouseTypes houseTypes) {
        var houseImage = House.create(houseTypes, AweS3Urls.URLFrontImage1);
        return houseRepository.save(houseImage);
    }

    @Override
    public House findById(String id) {
        return houseRepository.findById(id).orElseThrow(() -> new HouseNotFoundException(id));
    }

    @Override
    public List<House> findAll() {
        return houseRepository.findAll();
    }

}
