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
            var house7 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house7.setLocation("Spain, Málaga");
            house7.setNumberRooms(3);
            house7.setBeds(2);
            house7.setPrice("$969 384");
            var house8 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house8.setLocation("Spain, Málaga");
            house8.setNumberRooms(3);
            house8.setBeds(2);
            house8.setPrice("$969 384");
            var house9 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house9.setLocation("Spain, Málaga");
            house9.setNumberRooms(3);
            house9.setBeds(2);
            house9.setPrice("$969 384");
            var house10 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house10.setLocation("Spain, Málaga");
            house10.setNumberRooms(3);
            house10.setBeds(2);
            house10.setPrice("$969 384");
            var house11 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house11.setLocation("Spain, Málaga");
            house11.setNumberRooms(3);
            house11.setBeds(2);
            house11.setPrice("$969 384");
            var house12 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house12.setLocation("Spain, Málaga");
            house12.setNumberRooms(3);
            house12.setBeds(2);
            house12.setPrice("$969 384");
            var house13 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house13.setLocation("Spain, Málaga");
            house13.setNumberRooms(3);
            house13.setBeds(2);
            house13.setPrice("$969 384");
            var house14 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house14.setLocation("Spain, Málaga");
            house14.setNumberRooms(3);
            house14.setBeds(2);
            house14.setPrice("$969 384");
            var house15 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house15.setLocation("Spain, Málaga");
            house15.setNumberRooms(3);
            house15.setBeds(2);
            house15.setPrice("$969 384");
            var house16 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house16.setLocation("Spain, Málaga");
            house16.setNumberRooms(3);
            house16.setBeds(2);
            house16.setPrice("$969 384");
            var house17 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house17.setLocation("Spain, Málaga");
            house17.setNumberRooms(3);
            house17.setBeds(2);
            house17.setPrice("$969 384");
            var house18 = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage6);
            house18.setLocation("Spain, Málaga");
            house18.setNumberRooms(3);
            house18.setBeds(2);
            house18.setPrice("$969 384");

            saveHouseWithImages(houseImageRepository, houseRepository, house1);
            saveHouseWithImages(houseImageRepository, houseRepository, house2);
            saveHouseWithImages(houseImageRepository, houseRepository, house3);
            saveHouseWithImages(houseImageRepository, houseRepository, house4);
            saveHouseWithImages(houseImageRepository, houseRepository, house5);
            saveHouseWithImages(houseImageRepository, houseRepository, house6);
            saveHouseWithImages(houseImageRepository, houseRepository, house7);
            saveHouseWithImages(houseImageRepository, houseRepository, house8);
            saveHouseWithImages(houseImageRepository, houseRepository, house9);
            saveHouseWithImages(houseImageRepository, houseRepository, house10);
            saveHouseWithImages(houseImageRepository, houseRepository, house11);
            saveHouseWithImages(houseImageRepository, houseRepository, house12);
            saveHouseWithImages(houseImageRepository, houseRepository, house13);
            saveHouseWithImages(houseImageRepository, houseRepository, house14);
            saveHouseWithImages(houseImageRepository, houseRepository, house15);
            saveHouseWithImages(houseImageRepository, houseRepository, house16);
            saveHouseWithImages(houseImageRepository, houseRepository, house17);
            saveHouseWithImages(houseImageRepository, houseRepository, house18);

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
