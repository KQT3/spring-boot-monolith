package com.chaincuerealestate.chaincuerealestate;

import com.chaincuerealestate.chaincuerealestate.domains.Broker;
import com.chaincuerealestate.chaincuerealestate.domains.Country;
import com.chaincuerealestate.chaincuerealestate.domains.House;
import com.chaincuerealestate.chaincuerealestate.domains.HouseImage;
import com.chaincuerealestate.chaincuerealestate.repositories.BrokerRepository;
import com.chaincuerealestate.chaincuerealestate.repositories.CountryRepository;
import com.chaincuerealestate.chaincuerealestate.repositories.HouseImageRepository;
import com.chaincuerealestate.chaincuerealestate.repositories.HouseRepository;
import com.chaincuerealestate.chaincuerealestate.utilities.AweS3Urls;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class ChaincueRealEstateApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChaincueRealEstateApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(
            BrokerRepository brokerRepository,
            HouseImageRepository houseImageRepository,
            HouseRepository houseRepository,
            CountryRepository countryRepository
    ) {
        return args -> {
            var broker1 = Broker.create("");
            var houseImage1 = HouseImage.create("url");
            var house1 = House.create(House.HouseTypes.VILLA, AweS3Urls.URL1);

            brokerRepository.save(broker1);
            houseImageRepository.save(houseImage1);
            houseRepository.save(house1);

            Optional<Country> countrySweden = countryRepository.findByName(Country.CountryNames.SWEDEN.toString());
            Optional<Country> countrySpain = countryRepository.findByName(Country.CountryNames.SPAIN.toString());

            if (countrySweden.isEmpty()) {
                var country1 = Country.create(Country.CountryNames.SWEDEN);
                countryRepository.save(country1);
            }

            if (countrySpain.isEmpty()) {
                var country2 = Country.create(Country.CountryNames.SPAIN);
                countryRepository.save(country2);
            }
        };
    }
}
