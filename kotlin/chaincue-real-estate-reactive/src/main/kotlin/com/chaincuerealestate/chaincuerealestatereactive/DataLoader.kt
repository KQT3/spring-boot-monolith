package com.chaincuerealestate.chaincuerealestatereactive

import com.chaincuerealestate.chaincuerealestatereactive.domains.*
import com.chaincuerealestate.chaincuerealestatereactive.repositories.*
import com.chaincuerealestate.chaincuerealestatereactive.utilities.AweS3Urls
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class DataLoader(
    private val brokerRepository: BrokerRepository,
    private val houseImageRepository: HouseImageRepository,
    private val houseRepository: HouseRepository,
    private val countryRepository: CountryRepository,
    private val houseImagesRepository: HouseImagesRepository
) {
    @Bean
    fun init(): ApplicationRunner {
        return ApplicationRunner {
            runBlocking {
                brokerRepository.deleteAll()
                countryRepository.deleteAll()
                houseRepository.deleteAll()
                houseImageRepository.deleteAll()
                houseImagesRepository.deleteAll()

                /*House*/
                val houseList = (1..18).map {
                    val house = House.create(House.HouseTypes.VILLA, AweS3Urls.URLFrontImage1)
                    house.location = "Spain, Málaga"
                    house.numberRooms = 3
                    house.beds = 2
                    house.price = "$969 384"
                    house
                }
                houseList.forEach {
                    saveHouseWithImages(
                        houseImageRepository,
                        houseImagesRepository,
                        houseRepository,
                        it
                    )
                }

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
    }
}

private suspend fun saveHouseWithImages(
    houseImageRepository: HouseImageRepository,
    houseImagesRepository: HouseImagesRepository,
    houseRepository: HouseRepository,
    house: House
) {
    houseRepository.save(house)
    val imageUrls = listOf(
        house.src,
        AweS3Urls.URL1,
        AweS3Urls.URL2,
        AweS3Urls.URL3,
        AweS3Urls.URL4,
        AweS3Urls.URL5,
        AweS3Urls.URL6
    )
    val imageList = mutableListOf<HouseImage>()

    for (url in imageUrls) {
        val houseImage = HouseImage.create(url)
        houseImageRepository.save(houseImage)
        imageList.add(houseImage)

        val houseImageAssociation = HouseImages.create(house.id, houseImage.id)
        houseImagesRepository.save(houseImageAssociation)
    }
}

