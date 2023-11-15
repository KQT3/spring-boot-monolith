package com.chaincuerealestate.chaincuerealestatereactive.domains

import org.springframework.data.relational.core.mapping.Table

@Table("house_images")
data class HouseImages(
    var houseId: String,
    var imageId: String
) : AbstractDomain() {
    companion object {
        fun create(houseId: String, imageId: String): HouseImages {
            return HouseImages(
                houseId,
                imageId
            )
        }
    }
}
