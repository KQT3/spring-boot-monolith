package com.example.chaincuerealestate.routes.houses

class HousesPageDTO(
    val countries: Array<Country>,
    val houses: Array<House>
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
