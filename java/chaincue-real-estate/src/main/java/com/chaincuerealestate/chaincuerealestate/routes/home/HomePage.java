package com.chaincuerealestate.chaincuerealestate.routes.home;

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
@RequestMapping("home")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class HomePage {
    private final HouseHelper houseHelper;
    private final CountryHelper countryHelper;

    @GetMapping
    public ResponseEntity<HomePageDTO> homePage() {
        log.info("HomePage");
        var toDTO = toHomePageDTO(Optional.empty());
        return ResponseEntity.ok(toDTO);
    }

    private HomePageDTO toHomePageDTO(Optional<Function<DTOBuilder, DTOBuilder>> additionalProcessing) {
        return Stream.of(new DTOBuilder())
                .map(additionalProcessing.orElseGet(Function::identity))
                .map(countryHelper.updateDTOBuilderWithCountries(DTOBuilder::setCountries))
                .map(houseHelper.updateDTOBuilderWithHouses(DTOBuilder::setHouses))
                .map(HomePage::toDTO)
                .findFirst().get();
    }

    private static HomePageDTO toDTO(DTOBuilder dtoBuilder) {
        return new HomePageDTO(
                dtoBuilder.getCountries().stream().map(HomePage::toDTO).toArray(HomePageDTO.Country[]::new),
                dtoBuilder.getHouses().stream().map(HomePage::toDTO).toArray(HomePageDTO.House[]::new));
    }

    private static HomePageDTO.Country toDTO(Country country) {
        return new HomePageDTO.Country(
                country.getName()
        );
    }

    private static HomePageDTO.House toDTO(House house) {
        return new HomePageDTO.House(
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

        List<House> getHouses() {
            return houses.subList(0, 6);
        }
    }

}
