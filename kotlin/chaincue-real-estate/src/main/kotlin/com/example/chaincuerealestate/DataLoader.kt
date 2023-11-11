package com.example.chaincuerealestate


import com.example.chaincuerealestate.domains.Broker
import com.example.chaincuerealestate.domains.Country
import com.example.chaincuerealestate.domains.House
import com.example.chaincuerealestate.domains.HouseImage
import com.example.chaincuerealestate.repositories.BrokerRepository
import com.example.chaincuerealestate.repositories.CountryRepository
import com.example.chaincuerealestate.repositories.HouseImageRepository
import com.example.chaincuerealestate.repositories.HouseRepository
import com.example.chaincuerealestate.utilities.AweS3Urls
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
class DataLoader(
    private val brokerRepository: BrokerRepository,
    private val houseImageRepository: HouseImageRepository,
    private val houseRepository: HouseRepository,
    private val countryRepository: CountryRepository) {

    @Bean
    fun applicationRunner(): ApplicationRunner {
        return ApplicationRunner {
            brokerRepository.deleteAll()
            countryRepository.deleteAll()
            houseRepository.deleteAll()
            houseImageRepository.deleteAll()

            /*House*/
            val houseList = (1..18).map {
                val house = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage1)
                house.location = "Spain, Málaga"
                house.numberRooms = 3
                house.beds = 2
                house.price = "$969 384"
                house
            }

            houseList.forEach { saveHouseWithImages(houseImageRepository, houseRepository, it) }

            /*Broker*/
            val broker1 = Broker.create("")
            brokerRepository.save(broker1)

            /*Country*/
            val country1 = Country.create(Country.CountryNames.SWEDEN)
            countryRepository.save(country1)
            val country2 = Country.create(Country.CountryNames.SPAIN)
            countryRepository.save(country2)
        }
    }

    private fun saveHouseWithImages(
        houseImageRepository: HouseImageRepository,
        houseRepository: HouseRepository,
        house: House
    ) {
        for (i in 0 until 6) {
            val houseImage1 = HouseImage.create(house.src)
            val houseImage2 = HouseImage.create(AweS3Urls.URL1)
            val houseImage3 = HouseImage.create(AweS3Urls.URL2)
            val houseImage4 = HouseImage.create(AweS3Urls.URL3)
            val houseImage5 = HouseImage.create(AweS3Urls.URL4)
            val houseImage6 = HouseImage.create(AweS3Urls.URL5)
            val houseImage7 = HouseImage.create(AweS3Urls.URL6)
            val imageList = listOf(
                houseImage1,
                houseImage2,
                houseImage3,
                houseImage4,
                houseImage5,
                houseImage6,
                houseImage7
            )
            house.images = imageList
            houseImageRepository.save(houseImage1)
            houseRepository.save(house)
        }
    }

}
