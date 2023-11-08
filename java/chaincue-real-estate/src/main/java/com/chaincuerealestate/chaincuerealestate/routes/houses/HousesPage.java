package com.chaincuerealestate.chaincuerealestate.routes.houses;

import com.chaincuerealestate.chaincuerealestate.domains.Country;
import com.chaincuerealestate.chaincuerealestate.domains.House;
import com.chaincuerealestate.chaincuerealestate.services.DTOBuilderHelpers.CountryHelper;
import com.chaincuerealestate.chaincuerealestate.services.DTOBuilderHelpers.HouseHelper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@RestController
@RequestMapping("houses")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class HousesPage {
    private final HouseHelper houseHelper;
    private final CountryHelper countryHelper;

    @GetMapping
    public ResponseEntity<HousesPageDTO> housesPage() {
        log.info("housesPage");
        var toDTO = toHomePageDTO(Optional.empty());
        return ResponseEntity.ok(toDTO);
    }

    private HousesPageDTO toHomePageDTO(Optional<Function<DTOBuilder, DTOBuilder>> additionalProcessing) {
        return Stream.of(new DTOBuilder())
                .map(additionalProcessing.orElseGet(Function::identity))
                .map(countryHelper.updateDTOBuilderWithCountries(DTOBuilder::setCountries))
                .map(houseHelper.updateDTOBuilderWithHouses(DTOBuilder::setHouses))
                .map(HousesPage::toDTO)
                .findFirst().get();
    }

    private static HousesPageDTO toDTO(DTOBuilder dtoBuilder) {
        return new HousesPageDTO(
                dtoBuilder.getCountries().stream().map(HousesPage::toDTO).toArray(HousesPageDTO.Country[]::new),
                dtoBuilder.getHouses().stream().map(HousesPage::toDTO).toArray(HousesPageDTO.House[]::new));
    }

    private static HousesPageDTO.Country toDTO(Country country) {
        return new HousesPageDTO.Country(
                country.getName()
        );
    }

    private static HousesPageDTO.House toDTO(House house) {
        return new HousesPageDTO.House(
                house.getId(),
                house.getTitle(),
                house.getLocation(),
                house.getNumberRooms(),
                house.getBeds(),
                house.getPrice(),
                "₿32.346",
                house.getSrc()
        );
    }

    @Data
    private static class DTOBuilder {
        private List<Country> countries = new ArrayList<>();
        private List<House> houses = new ArrayList<>();

        public DTOBuilder() {
        }
    }
}
