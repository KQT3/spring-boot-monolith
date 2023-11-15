package com.chaincuerealestate.chaincuerealestatereactive.domains

import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.lang.NonNull
import java.time.LocalDateTime

@Table("house")
data class House(
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
    @ReadOnlyProperty
    var images: List<HouseImage> = listOf(),
    @Column("broker_id")
    var brokerId: String?,
    var created: LocalDateTime
) : AbstractDomain() {
    companion object {
        fun create(houseTypes: HouseTypes, src: String): House {
            return House(
                title = "",
                description = "",
                location = "",
                numberRooms = 0,
                beds = 0,
                price = "",
                src = src,
                sold = false,
                houseTypes = houseTypes.toString(),
                brokerId = null,
                created = LocalDateTime.now()
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
