package com.example.chaincuerealestate.routes.home

data class HomePageDTO(
    val countries: Array<Country>,
    val recentlyAddedHouses: Array<House>
) {
    data class Country(
        val name: String
    )

    data class House(
        val id: String,
        val title: String,
        val location: String,
        val numberRooms: Int,
        val beds: Int,
        val dollarPrice: String,
        val cryptoPrice: String,
        val src: String
    )
}
