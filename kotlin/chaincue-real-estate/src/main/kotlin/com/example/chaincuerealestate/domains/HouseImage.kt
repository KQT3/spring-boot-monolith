package com.example.chaincuerealestate.domains

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "house_image")
data class HouseImage(
    @Id
    var id: String,
    var url: String
) {
    companion object {
        fun create(url: String): HouseImage {
            return HouseImage(
                id = UUID.randomUUID().toString(),
                url = url
            )
        }
    }
}
