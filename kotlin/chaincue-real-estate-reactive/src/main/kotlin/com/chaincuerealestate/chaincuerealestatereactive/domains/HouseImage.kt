package com.chaincuerealestate.chaincuerealestatereactive.domains

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("house_image")
data class HouseImage(
    var url: String
) : AbstractDomain() {
    companion object {
        fun create(url: String): HouseImage {
            return HouseImage(
                url = url
            )
        }
    }
}
