package com.chaincuerealestate.chaincuerealestatereactive.domains

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("house_image")
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
