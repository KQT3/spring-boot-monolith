package com.chaincuerealestate.chaincuerealestate.services.DTOBuilderHelpers;

import com.chaincuerealestate.chaincuerealestate.domains.House;
import com.chaincuerealestate.chaincuerealestate.services.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class HouseHelper {
    private final HouseService houseService;

    public <B> Function<B, B> updateDTOBuilderWithHouseByHouse(Function<B, House> getHouse, BiConsumer<B, House> setHouse) {
        return dtoBuilder -> {
            House reservations = houseService.findById(getHouse.apply(dtoBuilder).getId());
            setHouse.accept(dtoBuilder, reservations);
            return dtoBuilder;
        };
    }

    public <B> Function<B, B> updateDTOBuilderWithHouses(BiConsumer<B, List<House>> setHouses) {
        return dtoBuilder -> {
            List<House> houses = houseService.findAll();
            setHouses.accept(dtoBuilder, houses);
            return dtoBuilder;
        };
    }
}
