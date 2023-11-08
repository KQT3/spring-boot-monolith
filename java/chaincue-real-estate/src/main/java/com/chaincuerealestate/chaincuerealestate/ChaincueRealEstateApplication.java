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

import java.util.ArrayList;
import java.util.List;

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
            brokerRepository.deleteAll();
            countryRepository.deleteAll();
            houseRepository.deleteAll();
            houseImageRepository.deleteAll();

            /*House*/
            var house1 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage1);
            house1.setLocation("Spain, Málaga");
            house1.setNumberRooms(3);
            house1.setBeds(2);
            house1.setPrice("$969 384");
            var house2 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage2);
            house2.setLocation("Spain, Málaga");
            house2.setNumberRooms(3);
            house2.setBeds(2);
            house2.setPrice("$969 384");
            var house3 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage3);
            house3.setLocation("Spain, Málaga");
            house3.setNumberRooms(3);
            house3.setBeds(2);
            house3.setPrice("$969 384");
            var house4 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage4);
            house4.setLocation("Spain, Málaga");
            house4.setNumberRooms(3);
            house4.setBeds(2);
            house4.setPrice("$969 384");
            var house5 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage5);
            house5.setLocation("Spain, Málaga");
            house5.setNumberRooms(3);
            house5.setBeds(2);
            house5.setPrice("$969 384");
            var house6 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house6.setLocation("Spain, Málaga");
            house6.setNumberRooms(3);
            house6.setBeds(2);
            house6.setPrice("$969 384");


            saveHouseWithImages(houseImageRepository, houseRepository, house1);
            saveHouseWithImages(houseImageRepository, houseRepository, house2);
            saveHouseWithImages(houseImageRepository, houseRepository, house3);
            saveHouseWithImages(houseImageRepository, houseRepository, house4);
            saveHouseWithImages(houseImageRepository, houseRepository, house5);
            saveHouseWithImages(houseImageRepository, houseRepository, house6);

            /*Broker*/
            var broker1 = Broker.create("");
            brokerRepository.save(broker1);

            /*Country*/
            var country1 = Country.create(Country.CountryNames.SWEDEN);
            countryRepository.save(country1);
            var country2 = Country.create(Country.CountryNames.SPAIN);
            countryRepository.save(country2);

        };
    }

    private static void saveHouseWithImages(HouseImageRepository houseImageRepository, HouseRepository houseRepository, House house) {
        for (int i = 0; i < 6; i++) {
            var houseImage1 = HouseImage.create(house.getSrc());
            var houseImage2 = HouseImage.create(AweS3Urls.URL1);
            var houseImage3 = HouseImage.create(AweS3Urls.URL2);
            var houseImage4 = HouseImage.create(AweS3Urls.URL3);
            var houseImage5 = HouseImage.create(AweS3Urls.URL4);
            var houseImage6 = HouseImage.create(AweS3Urls.URL5);
            var houseImage7 = HouseImage.create(AweS3Urls.URL6);
            List<HouseImage> imageList = new ArrayList<>();
            imageList.add(houseImage1);
            imageList.add(houseImage2);
            imageList.add(houseImage3);
            imageList.add(houseImage4);
            imageList.add(houseImage5);
            imageList.add(houseImage6);
            imageList.add(houseImage7);
            house.setImages(imageList);
            houseImageRepository.save(houseImage1);
            houseRepository.save(house);
        }
    }
}
