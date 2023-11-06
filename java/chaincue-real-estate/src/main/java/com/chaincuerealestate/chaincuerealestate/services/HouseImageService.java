package com.chaincuerealestate.chaincuerealestate.services;

import com.chaincuerealestate.chaincuerealestate.domains.HouseImage;
import com.chaincuerealestate.chaincuerealestate.exceptions.HouseImageNotFoundException;
import com.chaincuerealestate.chaincuerealestate.repositories.HouseImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseImageService implements HouseImageServiceI {
    private final HouseImageRepository houseImageRepository;

    @Override
    public HouseImage save(String url) {
        var houseImage = HouseImage.create(url);
        return houseImageRepository.save(houseImage);
    }

    @Override
    public HouseImage findById(String id) {
        return houseImageRepository.findById(id).orElseThrow(() -> new HouseImageNotFoundException(id));
    }

    @Override
    public List<HouseImage> findAll() {
        return houseImageRepository.findAll();
    }

}
