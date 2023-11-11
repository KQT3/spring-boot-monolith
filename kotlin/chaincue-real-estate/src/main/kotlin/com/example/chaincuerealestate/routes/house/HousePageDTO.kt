package com.example.chaincuerealestate.routes.house

data class HousePageDTO(
    val id: String,
    val title: String,
    val type: String,
    val location: String,
    val numberOfRooms: Int,
    val beds: Int,
    val dollarPrice: String,
    val cryptoPrice: String,
    val description: String,
    val images: Array<HouseImage>,
    val broker: Broker?
) {
    data class HouseImage(
        val id: String,
        val url: String
    )

    data class Broker(
        val id: String,
        val name: String,
        val phoneNumber: String,
        val email: String
    )
}
