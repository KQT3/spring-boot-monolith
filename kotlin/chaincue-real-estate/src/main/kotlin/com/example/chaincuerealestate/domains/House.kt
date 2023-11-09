package com.example.chaincuerealestate.domains

import jakarta.persistence.*
import lombok.NonNull
import java.time.LocalDateTime
import java.util.*

@Entity(name = "house")
data class House(
    @Id
    var id: String,
    var title: String,
    var description: String,
    var location: String,
    var numberRooms: Int,
    var beds: Int,
    var price: String,
    @get:NonNull
    var src: String,
    var sold: Boolean,
    @get:NonNull
    var houseTypes: String,
    @OneToMany(cascade = [CascadeType.ALL])
    var images: List<HouseImage>,
    @OneToOne(cascade = [CascadeType.ALL])
    var broker: Broker?,
    var timestamp: LocalDateTime
) {
    companion object {
        fun create(houseTypes: HouseTypes, src: String): House {
            return House(
                id = UUID.randomUUID().toString(),
                title = "",
                description = "",
                location = "",
                numberRooms = 0,
                beds = 0,
                price = "",
                src = src,
                sold = false,
                houseTypes = houseTypes.toString(),
                images = listOf(),
                broker = null,
                timestamp = LocalDateTime.now()
            )
        }
    }

    enum class HouseTypes {
        CONDOMINIUM,
        VILLA,
        TOWNHOUSE,
        VACATION_HOME,
        ESTATES_AND_FARMS,
        LAND,
        OTHER_HOUSES
    }
}
