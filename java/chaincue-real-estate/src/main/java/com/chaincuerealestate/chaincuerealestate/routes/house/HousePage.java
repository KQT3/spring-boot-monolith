package com.chaincuerealestate.chaincuerealestate.routes.house;

import com.chaincuerealestate.chaincuerealestate.domains.Broker;
import com.chaincuerealestate.chaincuerealestate.domains.House;
import com.chaincuerealestate.chaincuerealestate.domains.HouseImage;
import com.chaincuerealestate.chaincuerealestate.routes.home.HomePage;
import com.chaincuerealestate.chaincuerealestate.routes.home.HomePageDTO;
import com.chaincuerealestate.chaincuerealestate.services.DTOBuilderHelpers.CountryHelper;
import com.chaincuerealestate.chaincuerealestate.services.DTOBuilderHelpers.HouseHelper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@RestController
@RequestMapping("house")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class HousePage {
    private final HouseHelper houseHelper;

    @GetMapping("{houseId}")
    public ResponseEntity<HousePageDTO> housePage(@PathVariable String houseId) {
        log.info("housePage");
        var toDTO = toHomePageDTO(houseId, Optional.empty());
        return ResponseEntity.ok(toDTO);
    }

    private HousePageDTO toHomePageDTO(String houseId, Optional<Function<DTOBuilder, DTOBuilder>> additionalProcessing) {
        return Stream.of(new DTOBuilder())
                .map(additionalProcessing.orElseGet(Function::identity))
                .map(houseHelper.updateDTOBuilderWithHouseByHouseId(houseId, DTOBuilder::setHouse))
                .map(HousePage::toDTO)
                .findFirst().get();
    }

    private static HousePageDTO toDTO(DTOBuilder dtoBuilder) {
        return new HousePageDTO(
                dtoBuilder.getHouse().getId(),
                dtoBuilder.getHouse().getTitle(),
                dtoBuilder.getHouse().getHouseTypes(),
                dtoBuilder.getHouse().getLocation(),
                dtoBuilder.getHouse().getNumberRooms(),
                dtoBuilder.getHouse().getBeds(),
                dtoBuilder.getHouse().getPrice(),
                "₿32.346",
                dtoBuilder.getHouse().getDescription(),
                dtoBuilder.getHouse().getImages().stream().map(HousePage::toDTO).toArray(HousePageDTO.HouseImage[]::new),
                Optional.ofNullable(dtoBuilder.getHouse().getBroker()).map(HousePage::toDTO).orElse(null)
        );
    }

    private static HousePageDTO.HouseImage toDTO(HouseImage houseImage) {
        return new HousePageDTO.HouseImage(
                houseImage.getId(),
                houseImage.getUrl()
        );
    }

    private static HousePageDTO.Broker toDTO(Broker broker) {
        return new HousePageDTO.Broker(
                broker.getId(),
                broker.getName(),
                broker.getPhoneNumber(),
                broker.getEmail()
        );
    }

    @Data
    private static class DTOBuilder {
        private House house;

        public DTOBuilder() {
        }

    }
}
